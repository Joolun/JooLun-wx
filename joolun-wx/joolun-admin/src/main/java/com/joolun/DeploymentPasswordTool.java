package com.joolun;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Deployment-only helper used by the release installer to hash the initial
 * administrator password without exposing a fixed password in an image.
 */
public final class DeploymentPasswordTool
{
    private DeploymentPasswordTool()
    {
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            throw new IllegalArgumentException("Usage: DeploymentPasswordTool <password-file>");
        }

        String password = Files.readString(Path.of(args[0]), StandardCharsets.UTF_8).trim();
        if (password.length() < 12)
        {
            throw new IllegalArgumentException("The initial administrator password is too short");
        }

        System.out.println(BCrypt.hashpw(password, BCrypt.gensalt(10)));
    }
}
