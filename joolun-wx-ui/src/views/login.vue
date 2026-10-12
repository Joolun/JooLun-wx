<template>
  <main class="login-page">
    <div class="ambient-light ambient-light--top"></div>
    <div class="ambient-light ambient-light--bottom"></div>

    <section class="login-shell">
      <aside class="brand-panel">
        <div class="brand-panel__grid"></div>
        <div class="brand-header">
          <div class="brand-mark" aria-hidden="true">
            <span></span>
            <span></span>
          </div>
          <div>
            <div class="brand-name">JooLun</div>
            <div class="brand-meta">乔伦科技 · 商城管理平台</div>
          </div>
        </div>

        <div class="brand-copy">
          <div class="brand-eyebrow">
            <span class="brand-eyebrow__dot"></span>
            全渠道数字化运营
          </div>
          <h1>让每一次用户触达<br />都清晰可见</h1>
          <p>一站式连接公众号、小程序与商城业务，让运营决策更简单、更高效。</p>
        </div>

        <div class="insight-card" aria-hidden="true">
          <div class="insight-card__header">
            <div>
              <span class="insight-card__label">今日交易趋势</span>
              <strong>¥ 28,640</strong>
            </div>
            <span class="trend-badge">↗ 18.6%</span>
          </div>
          <div class="chart-bars">
            <span style="--bar-height: 34%"></span>
            <span style="--bar-height: 48%"></span>
            <span style="--bar-height: 42%"></span>
            <span style="--bar-height: 66%"></span>
            <span style="--bar-height: 58%"></span>
            <span style="--bar-height: 82%"></span>
            <span class="is-active" style="--bar-height: 74%"></span>
          </div>
          <div class="insight-card__stats">
            <div><span>待发货订单</span><strong>36</strong></div>
            <div><span>今日新增会员</span><strong>128</strong></div>
          </div>
        </div>

        <div class="feature-list">
          <span><i></i>公众号运营</span>
          <span><i></i>小程序管理</span>
          <span><i></i>商城数据分析</span>
        </div>
      </aside>

      <section class="form-panel">
        <div class="mobile-brand">
          <div class="brand-mark" aria-hidden="true">
            <span></span>
            <span></span>
          </div>
          <strong>JooLun</strong>
        </div>

        <el-form
          ref="loginRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <div class="form-heading">
            <span class="form-kicker">ADMIN CONSOLE</span>
            <h2>欢迎回来</h2>
            <p>登录您的管理账户，继续处理今日业务。</p>
          </div>

          <label class="field-label" for="login-username">账号</label>
          <el-form-item prop="username">
            <el-input
              id="login-username"
              v-model="loginForm.username"
              size="large"
              autocomplete="username"
              placeholder="请输入登录账号"
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <svg-icon icon-class="user" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>

          <label class="field-label" for="login-password">密码</label>
          <el-form-item prop="password">
            <el-input
              id="login-password"
              v-model="loginForm.password"
              type="password"
              size="large"
              autocomplete="current-password"
              placeholder="请输入登录密码"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <svg-icon icon-class="password" class="input-icon" />
              </template>
            </el-input>
          </el-form-item>

          <template v-if="captchaEnabled">
            <label class="field-label" for="login-code">验证码</label>
            <el-form-item prop="code">
              <div class="captcha-control">
                <el-input
                  id="login-code"
                  v-model="loginForm.code"
                  size="large"
                  autocomplete="off"
                  placeholder="请输入验证码"
                  @keyup.enter="handleLogin"
                >
                  <template #prefix>
                    <svg-icon icon-class="validCode" class="input-icon" />
                  </template>
                </el-input>
                <button
                  type="button"
                  class="login-code"
                  title="点击刷新验证码"
                  aria-label="点击刷新验证码"
                  @click="getCode"
                >
                  <img :src="codeUrl" alt="验证码" class="login-code-img" />
                </button>
              </div>
            </el-form-item>
          </template>

          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
            <span>账户信息将被加密保存</span>
          </div>

          <el-form-item class="submit-item">
            <el-button
              :loading="loading"
              size="large"
              type="primary"
              class="login-button"
              @click.prevent="handleLogin"
            >
              <span v-if="!loading">登录管理后台</span>
              <span v-else>正在登录...</span>
              <span v-if="!loading" class="button-arrow">→</span>
            </el-button>
          </el-form-item>

          <div v-if="register" class="register-entry">
            还没有账号？
            <router-link class="link-type" :to="'/register'">立即注册</router-link>
          </div>

          <div class="security-note">
            <span class="security-note__icon">✓</span>
            <span>安全连接已启用，请妥善保管您的账户信息</span>
          </div>
        </el-form>

        <footer class="login-footer">
          <span>© {{ currentYear }} JooLun 乔伦软件</span>
          <a href="https://www.joolun.com" target="_blank" rel="noopener noreferrer">官方网站</a>
        </footer>
      </section>
    </section>
  </main>
</template>

<script setup>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import useUserStore from "@/store/modules/user";

const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();

const loginForm = ref({
  username: "test",
  password: "123456",
  rememberMe: false,
  code: "",
  uuid: "",
});

const loginRules = {
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }],
};

const codeUrl = ref("");
const loading = ref(false);
const currentYear = new Date().getFullYear();
// 验证码开关
const captchaEnabled = ref(true);
// 注册开关
const register = ref(false);
const redirect = ref(undefined);

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect;
  },
  { immediate: true }
);

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        Cookies.set("username", loginForm.value.username, { expires: 30 });
        Cookies.set("password", encrypt(loginForm.value.password), {
          expires: 30,
        });
        Cookies.set("rememberMe", loginForm.value.rememberMe, { expires: 30 });
      } else {
        // 否则移除
        Cookies.remove("username");
        Cookies.remove("password");
        Cookies.remove("rememberMe");
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then(() => {
          const query = route.query;
          const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
            if (cur !== "redirect") {
              acc[cur] = query[cur];
            }
            return acc;
          }, {});
          router.push({ path: redirect.value || "/", query: otherQueryParams });
        })
        .catch(() => {
          loading.value = false;
          // 重新获取验证码
          if (captchaEnabled.value) {
            getCode();
          }
        });
    }
  });
}

function getCode() {
  getCodeImg().then((res) => {
    captchaEnabled.value =
      res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password:
      password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
  };
}

getCode();
getCookie();
</script>

<style lang="scss" scoped>
.login-page {
  position: relative;
  display: flex;
  min-height: 100vh;
  padding: 36px;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  color: #172033;
  background:
    radial-gradient(circle at 12% 18%, rgba(72, 118, 255, 0.11), transparent 28%),
    radial-gradient(circle at 88% 82%, rgba(40, 199, 173, 0.1), transparent 26%),
    #f3f6fb;
  box-sizing: border-box;
}

.ambient-light {
  position: absolute;
  width: 420px;
  height: 420px;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(10px);

  &--top {
    top: -260px;
    right: 8%;
    background: rgba(55, 102, 246, 0.08);
  }

  &--bottom {
    bottom: -280px;
    left: 12%;
    background: rgba(31, 194, 161, 0.09);
  }
}

.login-shell {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: minmax(0, 1.08fr) minmax(440px, 0.92fr);
  width: min(1180px, 100%);
  min-height: 680px;
  overflow: hidden;
  border: 1px solid rgba(213, 221, 235, 0.82);
  border-radius: 28px;
  background: #ffffff;
  box-shadow: 0 30px 80px rgba(33, 48, 83, 0.16);
}

.brand-panel {
  position: relative;
  display: flex;
  flex-direction: column;
  padding: 52px 58px 44px;
  overflow: hidden;
  color: #ffffff;
  background:
    radial-gradient(circle at 88% 12%, rgba(104, 168, 255, 0.42), transparent 25%),
    linear-gradient(145deg, #101f58 0%, #173d9a 52%, #146bb2 100%);
}

.brand-panel::before,
.brand-panel::after {
  position: absolute;
  content: "";
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.brand-panel::before {
  width: 390px;
  height: 390px;
  right: -210px;
  top: 54px;
}

.brand-panel::after {
  width: 270px;
  height: 270px;
  left: -160px;
  bottom: -110px;
}

.brand-panel__grid {
  position: absolute;
  inset: 0;
  opacity: 0.12;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.22) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.22) 1px, transparent 1px);
  background-size: 42px 42px;
  mask-image: linear-gradient(to bottom, transparent 0%, #000 22%, #000 74%, transparent 100%);
}

.brand-header,
.brand-copy,
.insight-card,
.feature-list {
  position: relative;
  z-index: 1;
}

.brand-header {
  display: flex;
  align-items: center;
  gap: 13px;
}

.brand-mark {
  position: relative;
  display: grid;
  width: 46px;
  height: 46px;
  place-items: center;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.36);
  border-radius: 14px;
  background: linear-gradient(145deg, rgba(255, 255, 255, 0.26), rgba(255, 255, 255, 0.1));
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.28);

  span:first-child {
    width: 18px;
    height: 23px;
    border-right: 5px solid #ffffff;
    border-bottom: 5px solid #ffffff;
    border-radius: 0 0 8px 0;
    transform: translate(-3px, -2px);
  }

  span:last-child {
    position: absolute;
    width: 6px;
    height: 6px;
    right: 9px;
    top: 9px;
    border-radius: 50%;
    background: #57e1c3;
    box-shadow: 0 0 0 4px rgba(87, 225, 195, 0.14);
  }
}

.brand-name {
  font-size: 21px;
  font-weight: 700;
  line-height: 1.1;
  letter-spacing: 0.4px;
}

.brand-meta {
  margin-top: 5px;
  color: rgba(255, 255, 255, 0.64);
  font-size: 11px;
  letter-spacing: 1.6px;
}

.brand-copy {
  margin-top: 62px;

  h1 {
    margin: 19px 0 18px;
    font-size: clamp(36px, 3.3vw, 50px);
    font-weight: 650;
    line-height: 1.22;
    letter-spacing: -1.5px;
  }

  p {
    max-width: 470px;
    margin: 0;
    color: rgba(235, 243, 255, 0.68);
    font-size: 15px;
    line-height: 1.9;
  }
}

.brand-eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 9px;
  color: #bcd5ff;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 1.5px;
}

.brand-eyebrow__dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #54e1c2;
  box-shadow: 0 0 0 5px rgba(84, 225, 194, 0.13);
}

.insight-card {
  width: min(440px, 100%);
  margin-top: 34px;
  padding: 22px 24px 18px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: 18px;
  background: rgba(255, 255, 255, 0.11);
  box-shadow: 0 18px 44px rgba(3, 20, 65, 0.18);
  backdrop-filter: blur(14px);
  box-sizing: border-box;
}

.insight-card__header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;

  div {
    display: flex;
    flex-direction: column;
    gap: 7px;
  }

  strong {
    font-size: 24px;
    font-weight: 650;
    letter-spacing: 0.3px;
  }
}

.insight-card__label,
.insight-card__stats span {
  color: rgba(224, 236, 255, 0.63);
  font-size: 11px;
  letter-spacing: 0.7px;
}

.trend-badge {
  padding: 5px 9px;
  color: #9af1d7;
  font-size: 11px;
  font-weight: 600;
  border-radius: 20px;
  background: rgba(61, 212, 171, 0.14);
}

.chart-bars {
  display: flex;
  height: 82px;
  margin: 19px 0 16px;
  align-items: flex-end;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.12);

  span {
    flex: 1;
    height: var(--bar-height);
    min-height: 8px;
    border-radius: 5px 5px 1px 1px;
    background: rgba(143, 187, 255, 0.38);
  }

  .is-active {
    background: linear-gradient(to top, #4ec9e6, #6de2bd);
    box-shadow: 0 0 18px rgba(86, 218, 198, 0.25);
  }
}

.insight-card__stats {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;

  div {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  strong {
    font-size: 14px;
  }
}

.feature-list {
  display: flex;
  margin-top: auto;
  padding-top: 30px;
  gap: 24px;
  color: rgba(235, 243, 255, 0.7);
  font-size: 11px;

  span {
    display: flex;
    align-items: center;
    gap: 7px;
    white-space: nowrap;
  }

  i {
    width: 4px;
    height: 4px;
    border-radius: 50%;
    background: #55dfc1;
  }
}

.form-panel {
  display: flex;
  flex-direction: column;
  padding: 54px 64px 28px;
  background: #ffffff;
}

.mobile-brand {
  display: none;
  align-items: center;
  gap: 11px;
  color: #173d9a;
  font-size: 19px;

  .brand-mark {
    width: 40px;
    height: 40px;
    border-color: rgba(31, 91, 208, 0.22);
    background: linear-gradient(145deg, #315fcc, #1684c5);
  }
}

.login-form {
  width: 100%;
  max-width: 390px;
  margin: auto;
}

.form-heading {
  margin-bottom: 34px;

  h2 {
    margin: 10px 0 10px;
    color: #172033;
    font-size: 32px;
    font-weight: 650;
    line-height: 1.25;
    letter-spacing: -0.7px;
  }

  p {
    margin: 0;
    color: #8490a6;
    font-size: 13px;
    line-height: 1.7;
  }
}

.form-kicker {
  color: #2f6bda;
  font-size: 10px;
  font-weight: 700;
  letter-spacing: 2px;
}

.field-label {
  display: inline-block;
  margin-bottom: 8px;
  color: #3d485d;
  font-size: 12px;
  font-weight: 600;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 20px;
}

.login-form :deep(.el-input__wrapper) {
  min-height: 48px;
  padding: 0 15px;
  border: 1px solid #dfe5ef;
  border-radius: 11px;
  background: #f9fbfe;
  box-shadow: none;
  transition: border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: #b9c8e2;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #3473df;
  background: #ffffff;
  box-shadow: 0 0 0 3px rgba(52, 115, 223, 0.1);
}

.login-form :deep(.el-input__inner) {
  color: #273247;
  font-size: 13px;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #a9b2c2;
}

.login-form :deep(.el-form-item__error) {
  padding-top: 5px;
  font-size: 11px;
}

.input-icon {
  width: 15px;
  height: 15px;
  margin-right: 5px;
  color: #8e9bb0;
}

.captcha-control {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 126px;
  width: 100%;
  gap: 10px;
}

.login-code {
  height: 50px;
  padding: 0;
  overflow: hidden;
  cursor: pointer;
  border: 1px solid #dfe5ef;
  border-radius: 11px;
  background: #f5f7fb;
  transition: border-color 0.2s ease, transform 0.2s ease;

  &:hover {
    border-color: #8dacdf;
    transform: translateY(-1px);
  }
}

.login-code-img {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.form-options {
  display: flex;
  min-height: 24px;
  margin: -2px 0 24px;
  align-items: center;
  justify-content: space-between;
  gap: 16px;

  span {
    color: #a1aabc;
    font-size: 11px;
  }
}

.form-options :deep(.el-checkbox__label) {
  color: #606d82;
  font-size: 12px;
}

.submit-item {
  margin-bottom: 0 !important;
}

.login-button {
  width: 100%;
  height: 50px;
  padding: 0 20px;
  border: 0;
  border-radius: 11px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.6px;
  background: linear-gradient(105deg, #315fd0 0%, #2679db 55%, #178bbe 100%);
  box-shadow: 0 12px 26px rgba(41, 103, 205, 0.24);
  transition: transform 0.2s ease, box-shadow 0.2s ease;

  &:hover,
  &:focus {
    background: linear-gradient(105deg, #2856c4 0%, #1f70d2 55%, #1280b3 100%);
    box-shadow: 0 14px 30px rgba(41, 103, 205, 0.3);
    transform: translateY(-1px);
  }
}

.button-arrow {
  margin-left: auto;
  font-size: 18px;
  font-weight: 400;
}

.register-entry {
  margin-top: 18px;
  text-align: center;
  color: #8792a6;
  font-size: 12px;
}

.link-type {
  color: #2e6bd4;
  font-weight: 600;
}

.security-note {
  display: flex;
  margin-top: 22px;
  align-items: center;
  justify-content: center;
  gap: 7px;
  color: #a0a9b8;
  font-size: 10px;
}

.security-note__icon {
  display: inline-grid;
  width: 16px;
  height: 16px;
  place-items: center;
  color: #279b80;
  border-radius: 50%;
  background: #e9f8f3;
  font-size: 9px;
  font-weight: 700;
}

.login-footer {
  display: flex;
  margin-top: 30px;
  align-items: center;
  justify-content: center;
  gap: 15px;
  color: #abb3c0;
  font-size: 10px;

  a {
    color: #7e8ba0;
    text-decoration: none;

    &:hover {
      color: #2e6bd4;
    }
  }
}

@media (max-width: 980px) {
  .login-shell {
    grid-template-columns: minmax(0, 1fr) 440px;
  }

  .brand-panel {
    padding: 46px 40px 38px;
  }

  .feature-list {
    gap: 14px;
  }
}

@media (max-width: 820px) {
  .login-page {
    padding: 24px;
  }

  .login-shell {
    grid-template-columns: 1fr;
    width: min(520px, 100%);
    min-height: 650px;
  }

  .brand-panel {
    display: none;
  }

  .form-panel {
    padding: 36px 58px 26px;
  }

  .mobile-brand {
    display: flex;
  }
}

@media (max-width: 560px) {
  .login-page {
    padding: 0;
    align-items: stretch;
    background: #ffffff;
  }

  .login-shell {
    min-height: 100vh;
    border: 0;
    border-radius: 0;
    box-shadow: none;
  }

  .form-panel {
    padding: 28px 24px 22px;
  }

  .login-form {
    margin-top: 54px;
  }

  .form-heading {
    margin-bottom: 30px;

    h2 {
      font-size: 28px;
    }
  }

  .form-options span {
    display: none;
  }

  .captcha-control {
    grid-template-columns: minmax(0, 1fr) 112px;
  }
}

@media (max-height: 780px) and (min-width: 821px) {
  .login-page {
    padding-top: 18px;
    padding-bottom: 18px;
  }

  .login-shell {
    min-height: 640px;
  }

  .brand-panel {
    padding-top: 34px;
    padding-bottom: 28px;
  }

  .brand-copy {
    margin-top: 30px;
  }

  .brand-copy h1 {
    margin-top: 14px;
    margin-bottom: 12px;
  }

  .insight-card {
    margin-top: 22px;
    padding-top: 17px;
    padding-bottom: 14px;
  }

  .chart-bars {
    height: 64px;
    margin-top: 14px;
    margin-bottom: 12px;
  }

  .feature-list {
    padding-top: 18px;
  }

  .form-panel {
    padding-top: 26px;
    padding-bottom: 20px;
  }

  .form-heading {
    margin-bottom: 26px;
  }

  .login-form :deep(.el-form-item) {
    margin-bottom: 16px;
  }

  .form-options {
    margin-bottom: 18px;
  }

  .security-note {
    margin-top: 17px;
  }

  .login-footer {
    margin-top: 18px;
  }
}
</style>
