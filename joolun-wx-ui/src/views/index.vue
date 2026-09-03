<template>
  <main class="commercial-home">

    <section id="plans" class="section-block plans-section">
      <div class="page-shell">
        <div class="section-heading section-heading--center">
          <div class="section-kicker">COMMERCIAL EDITIONS</div>
          <h2>不是功能越多越好，适合当前阶段才重要</h2>
          <p>从公众号运营到企业级生态，四个商业版本覆盖不同经营复杂度</p>
          <h3>全系前沿主流架构：多租户Sass + SpringCloud微服务 + SpringBoot4 + Vue3 + JDK21+</h3>
        </div>
        <div class="plan-grid">
          <article v-for="plan in plans" :key="plan.name" class="plan-card" :class="{ featured: plan.featured }">
            <div v-if="plan.badge" class="plan-card__badge">{{ plan.badge }}</div>
            <div class="plan-card__index">{{ plan.index }}</div>
            <p class="plan-card__scene">{{ plan.scene }}</p>
            <h3>{{ plan.name }}</h3>
            <p class="plan-card__positioning">{{ plan.positioning }}</p>
            <div class="plan-card__demo">
              <span>演示环境</span>
              <a :href="`https://${plan.demoHost}/`" target="_blank" rel="noopener noreferrer" :aria-label="`${plan.name}演示环境（新窗口打开）`">
                {{ plan.demoHost }}<el-icon><Link /></el-icon>
              </a>
            </div>
            <div class="plan-card__divider"></div>
            <ul>
              <li v-for="feature in plan.features" :key="feature">
                <span><el-icon><Check /></el-icon></span>{{ feature }}
              </li>
            </ul>
            <button type="button" class="plan-card__action" @click="openConsult(plan.name)">
              咨询此版本<el-icon><ArrowRight /></el-icon>
            </button>
          </article>
        </div>
        <div class="comparison-link">
          <span>想逐项核对功能？</span>
          <button type="button" @click="goTarget(urls.comparison)">查看完整功能对比<el-icon><ArrowRight /></el-icon></button>
        </div>
      </div>
    </section>
    <section class="trust-strip">
      <div class="page-shell trust-strip__inner">
        <div class="trust-strip__title"><span>开源版已具备</span><strong>够用、易懂、好上手</strong></div>
        <div v-for="item in openSourceStrengths" :key="item.title" class="trust-item">
          <strong>{{ item.title }}</strong><span>{{ item.description }}</span>
        </div>
        <button class="text-link" type="button" @click="goTarget(urls.gitee)">
          查看开源仓库<el-icon><Link /></el-icon>
        </button>
      </div>
    </section>

    <section class="hero-section">
      <div class="hero-glow hero-glow--left"></div>
      <div class="hero-glow hero-glow--right"></div>
      <div class="page-shell hero-grid">
        <div class="hero-copy">
          <div class="edition-pill"><span></span>JooLun 开源版 · 轻量商城底座</div>
          <h1>面向不同业务阶段，<br /><em>选择更适合的商业版本</em></h1>
          <p class="hero-description">
            开源版适合源码学习、业务验证和单商城项目。当你开始需要多租户、多商户、多终端或供应链能力，JooLun 商业版为下一阶段提供完整产品路径。
          </p>
          <div class="hero-actions">
            <button class="primary-button" type="button" @click="scrollToSection('selector')">
              30 秒选版本<el-icon><ArrowRight /></el-icon>
            </button>
            <button class="secondary-button" type="button" @click="goTarget(urls.demo)">
              <el-icon><View /></el-icon>查看商业版演示
            </button>
          </div>
          <div class="hero-note">
            <span><el-icon><Check /></el-icon>全源码交付</span>
            <span><el-icon><Check /></el-icon>支持二次开发</span>
            <span><el-icon><Check /></el-icon>提供部署协助</span>
          </div>
        </div>

        <div class="upgrade-map" aria-label="产品升级路径">
          <div class="upgrade-map__header">
            <div><p>你的业务成长路径</p><span>从验证想法，到规模化经营</span></div>
            <div class="upgrade-map__mark">JL</div>
          </div>
          <div class="current-edition">
            <span class="current-edition__label">你正在使用</span>
            <div><strong>开源版</strong><small>单体架构 · 单商城 · 微信生态</small></div>
            <b>免费开源</b>
          </div>
          <div class="upgrade-line"><span>业务升级信号</span><i></i></div>
          <div class="upgrade-signals">
            <div v-for="signal in upgradeSignals" :key="signal.title" class="signal-item">
              <span class="signal-item__icon">{{ signal.icon }}</span>
              <div><strong>{{ signal.title }}</strong><p>{{ signal.description }}</p></div>
            </div>
          </div>
          <button class="map-link" type="button" @click="scrollToSection('plans')">
            查看对应解决方案<el-icon><ArrowRight /></el-icon>
          </button>
        </div>
      </div>
    </section>

    <section class="section-block capability-section">
      <div class="page-shell capability-grid">
        <div class="section-heading section-heading--light">
          <div class="section-kicker">WHEN TO UPGRADE</div>
          <h2>出现这些需求，<br />说明业务该升级了</h2>
          <p>商业版解决的不是“多几个菜单”，而是业务规模扩大后，组织、终端、交易和治理复杂度的系统性问题。</p>
          <button class="light-outline-button" type="button" @click="openConsult()">
            和产品顾问聊聊需求<el-icon><ArrowRight /></el-icon>
          </button>
        </div>
        <div class="capability-list">
          <article v-for="capability in capabilities" :key="capability.code" class="capability-card">
            <div class="capability-card__code">{{ capability.code }}</div>
            <div><h3>{{ capability.title }}</h3><p>{{ capability.description }}</p><span>{{ capability.edition }}</span></div>
          </article>
        </div>
      </div>
    </section>

    <section id="selector" class="section-block selector-section">
      <div class="page-shell selector-layout">
        <div class="selector-intro">
          <div class="section-kicker">30-SECOND SELECTOR</div>
          <h2>回答 4 个问题，<br />找到更合适的版本</h2>
          <p>选择最接近现状的答案，我们会根据业务边界给出初步建议。</p>
          <div class="selector-progress">
            <div><span>选型进度</span><strong>{{ answeredCount }}/4</strong></div>
            <i><span :style="{ width: `${answeredCount * 25}%` }"></span></i>
          </div>
          <div class="selector-tip"><b>TIP</b>如果只是学习源码、验证想法或经营单商城，继续使用开源版通常更合适。</div>
        </div>

        <div class="selector-panel">
          <div v-for="(question, questionIndex) in questions" :key="question.key" class="question-row">
            <div class="question-row__title"><span>0{{ questionIndex + 1 }}</span><strong>{{ question.title }}</strong></div>
            <div class="option-list">
              <button
                v-for="option in question.options"
                :key="option.value"
                type="button"
                :class="{ active: answers[question.key] === option.value }"
                :aria-pressed="answers[question.key] === option.value"
                @click="answers[question.key] = option.value"
              >{{ option.label }}</button>
            </div>
          </div>
          <div class="recommendation" :class="{ ready: recommendation }">
            <template v-if="recommendation">
              <div class="recommendation__label">初步建议</div>
              <div class="recommendation__main">
                <div><h3>{{ recommendation.name }}</h3><p>{{ recommendation.reason }}</p></div>
                <button type="button" @click="openConsult(recommendation.name)">获取方案<el-icon><ArrowRight /></el-icon></button>
              </div>
            </template>
            <div v-else class="recommendation__empty">
              <span>{{ answeredCount === 0 ? '完成选择后查看建议' : `再回答 ${4 - answeredCount} 个问题` }}</span><i></i>
            </div>
          </div>
          <p class="selector-disclaimer">选型结果仅供参考，具体交付模块及定制范围以正式版本清单和合同约定为准。</p>
        </div>
      </div>
    </section>

    <section class="page-shell final-cta">
      <div class="final-cta__copy">
        <div class="section-kicker">TALK TO US</div>
        <h2>把你的业务场景告诉我们，<br />少走一段重复建设的路</h2>
        <p>扫码添加产品顾问，获取版本清单、演示环境和针对你的选型建议。</p>
        <div class="final-cta__actions">
          <button class="white-button" type="button" @click="openConsult()"><el-icon><ChatDotRound /></el-icon>微信咨询商业版</button>
          <button class="ghost-button" type="button" @click="goTarget(urls.website)">访问 JooLun 官网<el-icon><ArrowRight /></el-icon></button>
        </div>
      </div>
      <div class="qr-card">
        <img src="/joolunkefu.jpeg" alt="JooLun 产品顾问微信二维码" />
        <div><strong>扫码咨询产品顾问</strong><span>备注“商业版咨询”</span></div>
      </div>
    </section>

    <footer class="home-footer"><div class="page-shell"><span>JooLun 开源版</span><p>轻量、开放、易二开 · 商业版能力以正式版本清单为准</p></div></footer>

    <button class="floating-consult" type="button" aria-label="咨询商业版" @click="openConsult()">
      <el-icon><ChatDotRound /></el-icon><span>咨询商业版</span>
    </button>

    <el-dialog v-model="consultVisible" width="420px" class="consult-dialog" append-to-body>
      <div class="consult-content">
        <div class="consult-content__mark"><el-icon><ChatDotRound /></el-icon></div>
        <h3>{{ consultTitle }}</h3>
        <p>微信扫码添加产品顾问，获取版本清单、产品演示与选型建议。</p>
        <img src="/joolunkefu.jpeg" alt="JooLun 产品顾问微信二维码" />
        <div class="consult-content__hint">添加时请备注“商业版咨询”</div>
        <button type="button" @click="goTarget(urls.demo)">暂不咨询，先看在线演示<el-icon><ArrowRight /></el-icon></button>
      </div>
    </el-dialog>
  </main>
</template>

<script setup name="ViewIndex">
import { computed, reactive, ref } from "vue";
import { ArrowRight, ChatDotRound, Check, Link, View } from "@element-plus/icons-vue";

const urls = {
  website: "https://www.joolun.com/",
  demo: "https://www.joolun.com/yshj/",
  comparison: "https://www.joolun.com/gndb.html",
  gitee: "https://gitee.com/joolun/JooLun-wx",
};

const consultVisible = ref(false);
const selectedPlan = ref("");

const upgradeSignals = [
  { icon: "01", title: "多个经营主体", description: "租户、商户或门店需要独立管理" },
  { icon: "02", title: "多端经营", description: "业务扩展到 H5、App 或 PC" },
  { icon: "03", title: "复杂交易治理", description: "分账、结算、供应商开始加入" },
];

const openSourceStrengths = [
  { title: "Spring Boot4 单体", description: "部署链路更短" },
  { title: "Vue 3 + 原生小程序", description: "前后端源码开放" },
  { title: "基础商城 + 微信管理", description: "验证核心业务闭环" },
];

const plans = [
  { index: "01", scene: "只需要公众号运营", name: "公众号基础版", demoHost: "demo.joolun.com", positioning: "面向多租户公众号运营，不包含商城和小程序管理。", features: ["Spring Cloud 微服务", "SaaS 多租户隔离", "公众号菜单、素材与消息", "多公众号统一管理"] },
  { index: "02", scene: "聚焦微信小程序成交", name: "小程序商城版", demoHost: "demo.joolun.com", positioning: "适合品牌商城、私域商城和微信小程序商城。", features: ["微服务与多租户", "商品、订单与常用营销", "商城可视化装修", "微信小程序商城"], badge: "微信成交推荐" },
  { index: "03", scene: "需要多店与多端经营", name: "Plus 多店版", demoHost: "demo1.joolun.com", positioning: "适合连锁零售、多商户商城和本地生活平台。", features: ["多商户入驻与多门店", "小程序、H5、App、PC", "商家运营 App", "微信、支付宝多场景支付"], badge: "多店经营推荐", featured: true },
  { index: "04", scene: "建设企业级商业生态", name: "Pro 旗舰版", demoHost: "pro.joolun.com", positioning: "适合 B2B2C、产业互联网和供应链平台。", features: ["支付分账与统一结算", "供应商商品与订单协同", "标准开放接口", "复杂组织与生态治理"], badge: "企业级" },
];

const capabilities = [
  { code: "SaaS", title: "多租户与微服务", description: "多个客户或业务组织需要数据隔离、独立运营，并具备持续扩展能力。", edition: "公众号基础版 / 小程序商城版起" },
  { code: "STORE", title: "多商户与多门店", description: "平台不再只是自己卖货，还要承载商户入驻、门店经营和分级权限。", edition: "Plus 多店版起" },
  { code: "OMNI", title: "全渠道经营", description: "消费者与商家需要同时覆盖小程序、H5、App、PC 和商家端。", edition: "Plus 多店版起" },
  { code: "ECOSYSTEM", title: "分账、供应链与开放生态", description: "业务进入支付分账、供应商协同、统一结算和平台化治理阶段。", edition: "Pro 旗舰版" },
];

const questions = [
  { key: "business", title: "你的核心业务是什么？", options: [{ label: "公众号运营", value: "official" }, { label: "商城交易", value: "commerce" }] },
  { key: "organization", title: "需要管理多少经营主体？", options: [{ label: "多个独立租户", value: "tenant" }, { label: "平台 + 商户 / 门店", value: "merchant" }] },
  { key: "terminal", title: "需要覆盖哪些用户终端？", options: [{ label: "微信生态为主", value: "wechat" }, { label: "H5 / App / PC 多端", value: "multi" }] },
  { key: "governance", title: "是否涉及支付分账、统一结算或供应商协同？", options: [{ label: "暂不涉及", value: "standard" }, { label: "需要企业级治理", value: "enterprise" }] },
];

const answers = reactive({ business: "", organization: "", terminal: "", governance: "" });
const answeredCount = computed(() => Object.values(answers).filter(Boolean).length);
const recommendation = computed(() => {
  if (answeredCount.value < 4) return null;
  if (answers.governance === "enterprise") return { name: "Pro 旗舰版", reason: "你需要支付分账、供应商协同或统一结算，建议从企业级平台治理能力出发。" };
  if (answers.organization === "merchant" || answers.terminal === "multi") return { name: "Plus 多店版", reason: "多商户、多门店或多终端经营是核心诉求，Plus 的业务边界更匹配。" };
  if (answers.business === "official") return { name: "公众号基础版", reason: "你目前聚焦公众号运营，不需要商城交易，可选择边界更精简的基础版本。" };
  return { name: "小程序商城版", reason: "你聚焦微信小程序成交，同时需要更完整的商城交付、多租户与装修能力。" };
});
const consultTitle = computed(() => selectedPlan.value ? `咨询 ${selectedPlan.value}` : "咨询 JooLun 商业版");

function scrollToSection(id) { document.getElementById(id)?.scrollIntoView({ behavior: "smooth", block: "start" }); }
function openConsult(planName = "") { selectedPlan.value = planName; consultVisible.value = true; }
function goTarget(href) {
  const target = window.open(href, "_blank", "noopener,noreferrer");
  if (target) target.opener = null;
}
</script>

<style lang="scss" scoped>
$ink: #17243f;
$muted: #66728a;
$primary: #1768e5;
$border: #e4e9f2;

.commercial-home { min-width: 0; overflow: hidden; color: $ink; background: #f7f9fc; font-family: Inter, "PingFang SC", "Microsoft YaHei", sans-serif; }
.page-shell { width: min(1180px, calc(100% - 64px)); margin: 0 auto; }
button { font-family: inherit; }
.hero-section { position: relative; padding: 72px 0 68px; overflow: hidden; background: linear-gradient(115deg, rgba(248,251,255,.98), rgba(238,246,255,.95)); }
.hero-section::before { position: absolute; inset: 0; pointer-events: none; content: ""; background-image: linear-gradient(rgba(23,104,229,.035) 1px, transparent 1px), linear-gradient(90deg, rgba(23,104,229,.035) 1px, transparent 1px); background-size: 46px 46px; mask-image: linear-gradient(to bottom, #000, transparent 85%); }
.hero-glow { position: absolute; width: 360px; height: 360px; border-radius: 50%; filter: blur(12px); opacity: .48; pointer-events: none; }
.hero-glow--left { top: -240px; left: -140px; background: #c8dcff; }
.hero-glow--right { right: -180px; bottom: -260px; background: #adcfff; }
.hero-grid { position: relative; z-index: 1; display: grid; grid-template-columns: minmax(0, 1.05fr) minmax(420px, .75fr); gap: 74px; align-items: center; }
.edition-pill { display: inline-flex; gap: 10px; align-items: center; padding: 8px 13px; color: #1659bd; font-size: 13px; font-weight: 700; letter-spacing: .5px; background: rgba(23,104,229,.08); border: 1px solid rgba(23,104,229,.13); border-radius: 999px; }
.edition-pill span { width: 7px; height: 7px; background: #2c7df2; border-radius: 50%; box-shadow: 0 0 0 5px rgba(44,125,242,.12); }
.hero-copy h1 { margin: 25px 0 20px; color: #14213c; font-size: clamp(38px, 4.2vw, 58px); font-weight: 800; line-height: 1.2; letter-spacing: -2.5px; }
.hero-copy h1 em { color: $primary; font-style: normal; }
.hero-description { max-width: 650px; margin: 0; color: #59667d; font-size: 17px; line-height: 1.85; }
.hero-actions, .final-cta__actions { display: flex; flex-wrap: wrap; gap: 12px; margin-top: 30px; }
.primary-button, .secondary-button, .white-button, .ghost-button, .light-outline-button { display: inline-flex; align-items: center; justify-content: center; gap: 9px; height: 48px; padding: 0 22px; font-size: 15px; font-weight: 700; border-radius: 8px; cursor: pointer; transition: .2s ease; }
.primary-button { color: #fff; background: $primary; border: 1px solid $primary; box-shadow: 0 10px 22px rgba(23,104,229,.22); }
.primary-button:hover { background: #0e5bcc; transform: translateY(-1px); }
.secondary-button { color: #23314c; background: rgba(255,255,255,.76); border: 1px solid #d7dfeb; }
.secondary-button:hover { color: $primary; background: #fff; border-color: #b8cceb; }
.hero-note { display: flex; flex-wrap: wrap; gap: 22px; margin-top: 25px; color: #778298; font-size: 13px; }
.hero-note span { display: inline-flex; gap: 6px; align-items: center; }
.hero-note .el-icon { color: #2aa676; font-size: 15px; }

.upgrade-map { position: relative; padding: 26px; background: rgba(255,255,255,.92); border: 1px solid rgba(214,225,240,.9); border-radius: 16px; box-shadow: 0 24px 70px rgba(37,69,117,.15); backdrop-filter: blur(10px); }
.upgrade-map::after { position: absolute; right: -9px; bottom: -9px; z-index: -1; width: 74%; height: 70%; content: ""; background: #d6e6ff; border-radius: 16px; }
.upgrade-map__header, .current-edition, .recommendation__main, .final-cta, .home-footer .page-shell { display: flex; align-items: center; justify-content: space-between; }
.upgrade-map__header p { margin: 0 0 6px; font-size: 17px; font-weight: 800; }
.upgrade-map__header span { color: #8993a5; font-size: 12px; }
.upgrade-map__mark { display: grid; width: 38px; height: 38px; color: #fff; font-size: 13px; font-weight: 800; background: $primary; border-radius: 9px; place-items: center; }
.current-edition { gap: 13px; margin-top: 22px; padding: 16px; background: #f3f7fd; border: 1px solid #e3ebf7; border-radius: 10px; }
.current-edition__label { flex: none; padding: 5px 8px; color: #65728a; font-size: 11px; background: #fff; border: 1px solid #dce4ef; border-radius: 5px; }
.current-edition > div { display: flex; flex: 1; flex-direction: column; gap: 4px; }
.current-edition strong { font-size: 15px; }
.current-edition small { color: #7b8698; font-size: 11px; }
.current-edition b { color: #22875f; font-size: 12px; }
.upgrade-line { display: flex; gap: 12px; align-items: center; margin: 20px 0 16px; color: #919bad; font-size: 11px; letter-spacing: 1px; }
.upgrade-line i { flex: 1; height: 1px; background: #e7ebf1; }
.upgrade-signals { display: grid; gap: 9px; }
.signal-item { display: flex; gap: 13px; align-items: center; padding: 10px 12px; border-radius: 8px; transition: background .2s ease; }
.signal-item:hover { background: #f7f9fc; }
.signal-item__icon { display: grid; flex: none; width: 34px; height: 34px; color: $primary; font-size: 11px; font-weight: 800; background: #edf4ff; border-radius: 8px; place-items: center; }
.signal-item strong { font-size: 13px; }
.signal-item p { margin: 3px 0 0; color: #8a94a6; font-size: 11px; }
.map-link { display: flex; align-items: center; justify-content: center; gap: 6px; width: 100%; margin-top: 18px; padding-top: 16px; color: $primary; font-size: 13px; font-weight: 700; background: transparent; border: 0; border-top: 1px solid #edf0f5; cursor: pointer; }

.trust-strip { background: #fff; border-top: 1px solid #edf0f4; border-bottom: 1px solid #edf0f4; }
.trust-strip__inner { display: grid; grid-template-columns: 1.15fr repeat(3, 1fr) auto; align-items: center; min-height: 100px; }
.trust-strip__title, .trust-item { display: flex; flex-direction: column; gap: 7px; }
.trust-strip__title span { color: #95a0b1; font-size: 12px; }
.trust-strip__title strong { font-size: 15px; }
.trust-item { position: relative; padding-left: 22px; }
.trust-item::before { position: absolute; top: 3px; bottom: 3px; left: 0; width: 1px; content: ""; background: #e8ebf0; }
.trust-item strong { font-size: 13px; }
.trust-item span { color: #929baa; font-size: 11px; }
.text-link, .comparison-link button { display: inline-flex; align-items: center; gap: 7px; color: $primary; font-size: 13px; font-weight: 700; background: transparent; border: 0; cursor: pointer; }

.section-block { padding: 90px 0; }
.plans-section { background: #f7f9fc; }
.section-heading--center { max-width: 720px; margin: 0 auto 42px; text-align: center; }
.section-kicker { margin-bottom: 13px; color: $primary; font-size: 11px; font-weight: 800; letter-spacing: 2px; }
.section-heading h2, .selector-intro h2, .final-cta h2 { margin: 0; color: $ink; font-size: clamp(30px, 3vw, 40px); font-weight: 800; line-height: 1.28; letter-spacing: -1.2px; }
.section-heading > p, .selector-intro > p { margin: 16px 0 0; color: $muted; font-size: 15px; line-height: 1.8; }
.plan-grid { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 14px; }
.plan-card { position: relative; display: flex; min-height: 470px; padding: 27px 24px 23px; overflow: hidden; flex-direction: column; background: #fff; border: 1px solid $border; border-radius: 12px; transition: .25s ease; }
.plan-card:hover { border-color: #b8cbe9; box-shadow: 0 16px 40px rgba(33,61,104,.1); transform: translateY(-4px); }
.plan-card.featured { border-color: rgba(23,104,229,.48); box-shadow: 0 14px 38px rgba(23,104,229,.11); }
.plan-card__badge { position: absolute; top: 0; right: 0; padding: 7px 12px; color: #1768e5; font-size: 10px; font-weight: 800; background: #eaf2ff; border-bottom-left-radius: 10px; }
.plan-card.featured .plan-card__badge { color: #fff; background: $primary; }
.plan-card__index { margin-bottom: 42px; color: #c3cbd7; font-size: 12px; font-weight: 800; letter-spacing: 1px; }
.plan-card__scene { margin: 0 0 10px; color: $primary; font-size: 11px; font-weight: 700; }
.plan-card h3 { margin: 0; font-size: 21px; font-weight: 800; }
.plan-card__positioning { min-height: 66px; margin: 13px 0 0; color: #748096; font-size: 13px; line-height: 1.7; }
.plan-card__demo { display: flex; flex-wrap: wrap; align-items: center; gap: 4px 8px; margin-top: 8px; font-size: 12px; line-height: 1.8; }
.plan-card__demo > span { color: #748096; font-size: 11px; }
.plan-card__demo a { display: inline-flex; align-items: center; gap: 5px; max-width: 100%; color: $primary; font-weight: 600; }
.plan-card__demo a:hover { color: #0e5bcc; text-decoration: underline; text-underline-offset: 3px; }
.plan-card__demo a:focus-visible { outline: 2px solid $primary; outline-offset: 3px; border-radius: 3px; }
.plan-card__divider { height: 1px; margin: 19px 0; background: #edf0f4; }
.plan-card ul { display: grid; gap: 13px; padding: 0; margin: 0 0 25px; list-style: none; }
.plan-card li { display: flex; gap: 9px; align-items: center; color: #4d5a70; font-size: 12px; }
.plan-card li > span { display: grid; flex: none; width: 18px; height: 18px; color: #24855f; font-size: 11px; background: #eaf7f1; border-radius: 50%; place-items: center; }
.plan-card__action { display: flex; align-items: center; justify-content: space-between; width: 100%; min-height: 42px; padding: 0 14px; margin-top: auto; color: #30405c; font-size: 12px; font-weight: 700; background: #f7f9fc; border: 1px solid #e7ebf1; border-radius: 7px; cursor: pointer; transition: .2s ease; }
.plan-card__action:hover, .plan-card.featured .plan-card__action { color: #fff; background: $primary; border-color: $primary; }
.comparison-link { display: flex; gap: 8px; align-items: center; justify-content: center; margin-top: 28px; color: #8b95a5; font-size: 12px; }

.capability-section { background: #14213b; }
.capability-grid { display: grid; grid-template-columns: .72fr 1.28fr; gap: 86px; align-items: center; }
.section-heading--light .section-kicker { color: #6ea8ff; }
.section-heading--light h2 { color: #fff; }
.section-heading--light > p { color: #aab6ca; }
.light-outline-button { height: 44px; margin-top: 27px; color: #dbe8fb; background: rgba(255,255,255,.03); border: 1px solid #42516b; }
.light-outline-button:hover { color: #fff; border-color: #769ad0; }
.capability-list { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.capability-card { display: grid; grid-template-columns: 62px 1fr; gap: 17px; min-height: 180px; padding: 24px; background: rgba(255,255,255,.045); border: 1px solid rgba(255,255,255,.09); border-radius: 10px; }
.capability-card__code { display: grid; align-self: start; width: 58px; height: 58px; color: #80b3ff; font-size: 9px; font-weight: 800; letter-spacing: .5px; background: rgba(55,126,231,.12); border: 1px solid rgba(92,153,244,.2); border-radius: 9px; place-items: center; }
.capability-card h3 { margin: 3px 0 9px; color: #fff; font-size: 16px; }
.capability-card p { margin: 0 0 13px; color: #a9b5c8; font-size: 12px; line-height: 1.7; }
.capability-card span { color: #6fa8ff; font-size: 10px; }

.selector-section { background: #fff; }
.selector-layout { display: grid; grid-template-columns: .67fr 1.33fr; gap: 78px; align-items: start; }
.selector-intro { position: sticky; top: 110px; }
.selector-progress { margin-top: 36px; }
.selector-progress > div { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; color: #8994a6; font-size: 12px; }
.selector-progress strong { color: $primary; }
.selector-progress > i { display: block; height: 6px; overflow: hidden; background: #edf1f6; border-radius: 99px; }
.selector-progress > i span { display: block; height: 100%; background: linear-gradient(90deg, $primary, #5c9cff); border-radius: inherit; transition: width .3s ease; }
.selector-tip { margin-top: 25px; padding: 16px 17px; color: #6f7b8f; font-size: 12px; line-height: 1.7; background: #f6f8fb; border-left: 3px solid #a9b7cb; border-radius: 0 7px 7px 0; }
.selector-tip b { display: block; margin-bottom: 4px; color: #36445e; font-size: 9px; letter-spacing: 1px; }
.selector-panel { padding: 0 30px 28px; background: #f9fafc; border: 1px solid #e6eaf0; border-radius: 14px; }
.question-row { padding: 25px 0; border-bottom: 1px solid #e7ebf1; }
.question-row__title { display: flex; gap: 12px; align-items: center; margin-bottom: 15px; }
.question-row__title span { color: #a1abbb; font-size: 10px; font-weight: 800; }
.question-row__title strong { font-size: 14px; }
.option-list { display: flex; flex-wrap: wrap; gap: 9px; padding-left: 27px; }
.option-list button { min-height: 38px; padding: 0 14px; color: #657187; font-size: 12px; background: #fff; border: 1px solid #dce2eb; border-radius: 7px; cursor: pointer; transition: .2s ease; }
.option-list button:hover { color: $primary; border-color: #a9c8f6; }
.option-list button.active { color: $primary; font-weight: 700; background: #edf4ff; border-color: $primary; box-shadow: inset 0 0 0 1px $primary; }
.recommendation { min-height: 88px; margin-top: 24px; padding: 20px; background: #eef1f5; border-radius: 10px; }
.recommendation.ready { color: #fff; background: linear-gradient(120deg, #1768e5, #1456c1); box-shadow: 0 12px 28px rgba(23,104,229,.18); }
.recommendation__label { margin-bottom: 11px; color: #bcd6ff; font-size: 9px; font-weight: 800; letter-spacing: 1.4px; }
.recommendation__main { gap: 24px; }
.recommendation__main h3 { margin: 0 0 5px; font-size: 18px; }
.recommendation__main p { max-width: 470px; margin: 0; color: #d9e8ff; font-size: 11px; line-height: 1.65; }
.recommendation__main button { display: inline-flex; flex: none; gap: 6px; align-items: center; height: 36px; padding: 0 13px; color: #155bc9; font-size: 11px; font-weight: 800; background: #fff; border: 0; border-radius: 6px; cursor: pointer; }
.recommendation__empty { display: flex; align-items: center; gap: 15px; height: 48px; color: #9aa4b4; font-size: 12px; }
.recommendation__empty i { flex: 1; height: 1px; background: #dce1e8; }
.selector-disclaimer { margin: 12px 0 0; color: #a0a9b8; font-size: 10px; text-align: center; }

.final-cta { position: relative; min-height: 330px; padding: 54px 62px; margin-top: 22px; margin-bottom: 80px; overflow: hidden; color: #fff; background: linear-gradient(120deg, #1768e5, #0f55bd 58%, #1049a0); border-radius: 16px; box-shadow: 0 22px 52px rgba(24,86,179,.18); }
.final-cta::before, .final-cta::after { position: absolute; content: ""; border: 1px solid rgba(255,255,255,.1); border-radius: 50%; }
.final-cta::before { top: -180px; right: 90px; width: 420px; height: 420px; }
.final-cta::after { right: -120px; bottom: -260px; width: 520px; height: 520px; }
.final-cta__copy, .qr-card { position: relative; z-index: 1; }
.final-cta .section-kicker { color: #b9d6ff; }
.final-cta h2 { color: #fff; }
.final-cta__copy > p { margin: 15px 0 0; color: #d5e6ff; font-size: 13px; }
.white-button { color: #155cc9; background: #fff; border: 1px solid #fff; }
.white-button:hover { box-shadow: 0 8px 20px rgba(0,0,0,.12); transform: translateY(-1px); }
.ghost-button { color: #e9f2ff; background: transparent; border: 1px solid rgba(255,255,255,.36); }
.ghost-button:hover { background: rgba(255,255,255,.08); border-color: rgba(255,255,255,.6); }
.qr-card { display: flex; flex: none; gap: 18px; padding: 17px; background: #fff; border-radius: 12px; box-shadow: 0 14px 30px rgba(0,27,75,.18); }
.qr-card img { width: 112px; height: 112px; object-fit: cover; }
.qr-card > div { display: flex; flex-direction: column; gap: 8px; padding-right: 8px; }
.qr-card strong { color: #22324d; font-size: 13px; }
.qr-card span { color: #8a95a6; font-size: 11px; }
.home-footer { color: #8993a4; background: #fff; border-top: 1px solid #e8ebf0; }
.home-footer .page-shell { min-height: 72px; }
.home-footer span { color: #2c3b56; font-size: 13px; font-weight: 800; }
.home-footer p { margin: 0; font-size: 11px; }
.floating-consult { position: fixed; right: 24px; bottom: 24px; z-index: 20; display: flex; gap: 8px; align-items: center; height: 46px; padding: 0 17px; color: #fff; font-size: 12px; font-weight: 700; background: #ff7043; border: 0; border-radius: 99px; box-shadow: 0 10px 28px rgba(255,112,67,.32); cursor: pointer; transition: .2s ease; }
.floating-consult:hover { background: #f46235; transform: translateY(-2px); }

:deep(.consult-dialog) { overflow: hidden; border-radius: 14px; }
:deep(.consult-dialog .el-dialog__header) { padding-bottom: 0; }
.consult-content { padding: 0 20px 20px; text-align: center; }
.consult-content__mark { display: grid; width: 46px; height: 46px; margin: 0 auto 13px; color: #fff; font-size: 21px; background: $primary; border-radius: 12px; place-items: center; }
.consult-content h3 { margin: 0; color: $ink; font-size: 21px; }
.consult-content > p { margin: 10px 0 16px; color: #758096; font-size: 12px; line-height: 1.7; }
.consult-content img { width: 190px; height: 190px; padding: 7px; object-fit: cover; border: 1px solid #e3e7ed; border-radius: 8px; }
.consult-content__hint { margin-top: 10px; color: #7d889a; font-size: 11px; }
.consult-content > button { display: inline-flex; align-items: center; gap: 5px; margin-top: 15px; color: $primary; font-size: 11px; font-weight: 700; background: transparent; border: 0; cursor: pointer; }

@media (max-width: 1100px) {
  .hero-grid { grid-template-columns: 1fr 390px; gap: 40px; }
  .plan-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .plan-card { min-height: 430px; }
  .trust-strip__inner { grid-template-columns: 1fr repeat(3, 1fr); }
  .trust-strip .text-link { display: none; }
  .capability-grid, .selector-layout { gap: 44px; }
}
@media (max-width: 860px) {
  .page-shell { width: min(720px, calc(100% - 36px)); }
  .hero-section { padding: 54px 0; }
  .hero-grid, .capability-grid, .selector-layout { grid-template-columns: 1fr; }
  .hero-grid { gap: 42px; }
  .trust-strip__inner { grid-template-columns: repeat(3, 1fr); gap: 18px; padding: 24px 0; }
  .trust-strip__title { grid-column: 1 / -1; }
  .selector-intro { position: static; }
  .final-cta { gap: 30px; padding: 46px 40px; }
}
@media (max-width: 620px) {
  .page-shell { width: calc(100% - 28px); }
  .hero-section { padding: 42px 0; }
  .hero-copy h1 { font-size: 34px; letter-spacing: -1.5px; }
  .hero-description { font-size: 15px; }
  .hero-actions, .hero-actions button { width: 100%; }
  .hero-note { gap: 12px 18px; }
  .upgrade-map { padding: 20px; }
  .current-edition { align-items: flex-start; flex-wrap: wrap; }
  .current-edition > b { margin-left: 47px; }
  .trust-strip__inner { grid-template-columns: 1fr; }
  .trust-item { padding: 13px 0 0; border-top: 1px solid #edf0f4; }
  .trust-item::before { display: none; }
  .section-block { padding: 64px 0; }
  .section-heading--center { text-align: left; }
  .plan-grid, .capability-list { grid-template-columns: 1fr; }
  .plan-card { min-height: 0; }
  .plan-card__index { margin-bottom: 26px; }
  .comparison-link { align-items: flex-start; flex-direction: column; }
  .selector-panel { padding: 0 18px 20px; }
  .option-list { padding-left: 0; }
  .option-list button { flex: 1 1 calc(50% - 8px); }
  .recommendation__main { align-items: flex-start; flex-direction: column; }
  .final-cta { align-items: flex-start; padding: 38px 25px; margin-bottom: 54px; flex-direction: column; }
  .final-cta__actions, .final-cta__actions button, .qr-card { width: 100%; }
  .qr-card { justify-content: center; }
  .home-footer .page-shell { gap: 7px; align-items: flex-start; justify-content: center; flex-direction: column; }
  .floating-consult { right: 14px; bottom: 14px; width: 46px; padding: 0; justify-content: center; }
  .floating-consult span { display: none; }
  :deep(.consult-dialog) { width: calc(100% - 30px) !important; }
}
</style>
