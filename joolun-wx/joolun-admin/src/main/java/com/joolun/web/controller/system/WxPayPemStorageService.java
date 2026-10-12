/**
 * Copyright (C) 2026
 * All rights reserved, Designed By www.joolun.com
 */
package com.joolun.web.controller.system;

import com.joolun.common.config.JooLunConfig;
import com.joolun.common.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信支付 PEM 密钥存储服务。
 * 密钥保存在独立的 certPath 目录，并阻止该目录落入 /profile/** 静态资源映射范围。
 *
 * @author www.joolun.com
 */
@Service
public class WxPayPemStorageService {

	private static final long MAX_PEM_SIZE = 16 * 1024L;
	private static final String PRIVATE_KEY_FILE_NAME = "apiclient_key.pem";
	private static final String PUBLIC_KEY_FILE_NAME = "pub_key.pem";
	private static final Pattern PKCS8_PRIVATE_KEY_PATTERN = Pattern.compile(
			"\\A\\s*-----BEGIN PRIVATE KEY-----\\s*([A-Za-z0-9+/=\\s]+)\\s*-----END PRIVATE KEY-----\\s*\\z");
	private static final Pattern PUBLIC_KEY_PATTERN = Pattern.compile(
			"\\A\\s*-----BEGIN PUBLIC KEY-----\\s*([A-Za-z0-9+/=\\s]+)\\s*-----END PUBLIC KEY-----\\s*\\z");

	/**
	 * 校验并保存商户 API 私钥，返回供 WxJava 使用的绝对路径。
	 */
	public String storeMerchantPrivateKey(MultipartFile file) {
		byte[] content = readPemFile(file, "商户 API 私钥");
		validatePrivateKey(content);
		return storePem(content, PRIVATE_KEY_FILE_NAME, ".apiclient_key_", "商户 API 私钥");
	}

	/**
	 * 校验并保存微信支付公钥，返回供 WxJava 使用的绝对路径。
	 */
	public String storeWechatPayPublicKey(MultipartFile file) {
		byte[] content = readPemFile(file, "微信支付公钥");
		validatePublicKey(content);
		return storePem(content, PUBLIC_KEY_FILE_NAME, ".pub_key_", "微信支付公钥");
	}

	private String storePem(byte[] content, String fileName, String temporaryPrefix, String label) {

		Path temporaryFile = null;
		try {
			Path storageDirectory = resolveStorageDirectory();
			Files.createDirectories(storageDirectory);
			Path target = storageDirectory.resolve(fileName).normalize();
			if (!target.getParent().equals(storageDirectory) || Files.isSymbolicLink(target)) {
				throw new ServiceException(label + "存储路径不安全，请联系管理员检查服务器目录");
			}

			temporaryFile = Files.createTempFile(storageDirectory, temporaryPrefix, ".tmp");
			Files.write(temporaryFile, content);
			applyOwnerOnlyPermissions(temporaryFile);
			try {
				Files.move(temporaryFile, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
			}
			catch (AtomicMoveNotSupportedException ignored) {
				Files.move(temporaryFile, target, StandardCopyOption.REPLACE_EXISTING);
			}
			applyOwnerOnlyPermissions(target);
			return target.toString();
		}
		catch (ServiceException e) {
			throw e;
		}
		catch (IOException e) {
			throw new ServiceException("保存" + label + "失败，请检查服务器证书目录权限")
					.setDetailMessage(e.getMessage());
		}
		finally {
			if (temporaryFile != null) {
				try {
					Files.deleteIfExists(temporaryFile);
				}
				catch (IOException ignored) {
					// 临时文件清理由操作系统或运维人员处理，不覆盖原始异常。
				}
			}
		}
	}

	private byte[] readPemFile(MultipartFile file, String label) {
		if (file == null || file.isEmpty()) {
			throw new ServiceException("请选择" + label + " PEM 文件");
		}
		if (file.getSize() > MAX_PEM_SIZE) {
			throw new ServiceException(label + "文件不能超过 16KB");
		}
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || !originalFilename.toLowerCase(Locale.ROOT).endsWith(".pem")) {
			throw new ServiceException("仅支持上传 .pem 格式的" + label);
		}
		try {
			return file.getBytes();
		}
		catch (IOException e) {
			throw new ServiceException("读取" + label + "失败，请重新上传").setDetailMessage(e.getMessage());
		}
	}

	private void validatePrivateKey(byte[] content) {
		try {
			String pem = new String(content, StandardCharsets.US_ASCII);
			Matcher matcher = PKCS8_PRIVATE_KEY_PATTERN.matcher(pem);
			if (!matcher.matches()) {
				throw new ServiceException("文件不是有效的 PKCS#8 商户私钥，请上传微信支付下载的 apiclient_key.pem");
			}
			byte[] keyBytes = Base64.getMimeDecoder().decode(matcher.group(1));
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
			if (!(privateKey instanceof RSAPrivateKey rsaPrivateKey) || rsaPrivateKey.getModulus().bitLength() < 2048) {
				throw new ServiceException("商户私钥必须是至少 2048 位的 RSA 私钥");
			}
		}
		catch (ServiceException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException("无法解析商户私钥，请确认上传的是未加密的 apiclient_key.pem")
					.setDetailMessage(e.getMessage());
		}
	}

	private void validatePublicKey(byte[] content) {
		try {
			String pem = new String(content, StandardCharsets.US_ASCII);
			Matcher matcher = PUBLIC_KEY_PATTERN.matcher(pem);
			if (!matcher.matches()) {
				throw new ServiceException("文件不是有效的微信支付公钥，请上传商户平台下载的 pub_key.pem");
			}
			byte[] keyBytes = Base64.getMimeDecoder().decode(matcher.group(1));
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
			if (!(publicKey instanceof RSAPublicKey rsaPublicKey) || rsaPublicKey.getModulus().bitLength() < 2048) {
				throw new ServiceException("微信支付公钥必须是至少 2048 位的 RSA 公钥");
			}
		}
		catch (ServiceException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServiceException("无法解析微信支付公钥，请确认上传的是商户平台下载的 pub_key.pem")
					.setDetailMessage(e.getMessage());
		}
	}

	private Path resolveStorageDirectory() {
		String profile = JooLunConfig.getProfile();
		if (profile == null || profile.isBlank()) {
			throw new ServiceException("未配置 joolun.profile，无法确定微信支付证书存储目录");
		}
		String certPath = JooLunConfig.getCertPath();
		if (certPath == null || certPath.isBlank()) {
			throw new ServiceException("未配置 joolun.certPath，无法确定微信支付证书存储目录");
		}
		Path profilePath = Paths.get(profile).toAbsolutePath().normalize();
		Path storageDirectory = Paths.get(certPath).toAbsolutePath().normalize().resolve("wxpay").normalize();
		if (storageDirectory.startsWith(profilePath)) {
			throw new ServiceException("joolun.certPath 不能位于公开上传目录 joolun.profile 内");
		}
		return storageDirectory;
	}

	private void applyOwnerOnlyPermissions(Path file) throws IOException {
		try {
			Files.setPosixFilePermissions(file, PosixFilePermissions.fromString("rw-------"));
		}
		catch (UnsupportedOperationException ignored) {
			// Windows 文件系统不支持 POSIX 权限，继续依赖所在目录的 ACL。
		}
	}
}
