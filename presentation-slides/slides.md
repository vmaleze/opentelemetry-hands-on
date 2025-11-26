---
theme: default
colorSchema: light
favicon: /images/otel-logo.png
title: OpenTelemetry Hands On
titleTemplate: '%s - OpenTelemetry Hands On'
drawings:
  persist: false
transition: slide-left
mdc: true # enable MDC Syntax: https://sli.dev/features/mdc
lineNumbers: true
background: https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=1920&q=80
backgroundSize: cover
class: text-center # apply unocss classes to the current slide
layout: cover
---

<div class="cover-content">

# <span class="title-text">OpenTelemetry Hands On</span>

## <span class="subtitle-text">2h chrono pour traquer tout ce qui bouge dans ton backend avec OpenTelemetry</span>

<div class="duration-text">Durée : 2 heures</div>

![Logo IPPON](/images/ippon-logo.svg)

</div>

---
layout: default
class: no-title-spacing
---

<div class="prerequisites-section">

## 💻 Pre-requisites

<div class="prerequisites-columns">
<div>

- Admin rights on the OS session
- IDE like IntelliJ or Visual Studio Code with Java support
- PowerShell 7 or Bash
- Docker

</div>
<div>

- Git and a [GitHub](https://github.com/) account
- [mise](https://mise.jdx.dev/getting-started.html)
- [Headlamp](https://headlamp.dev/)

</div>
</div>

</div>

<div class="setup-section">

## 🚀 Setup before starting

1. **Fork** https://github.com/vmaleze/opentelemetry-hands-on

2. Run `mise install` at the root of the repository.

3. Depending on your OS, run the according script to setup the environment (`setup.sh` or `setup.ps1`).
</div>

---
layout: about-speaker
speakerPhotoPath: /images/speaker-hugo.jpg
speakerName: Hugo JOUBERT
speakerJobTitle: DevOps Engineer / Cloud Builder
companyLogoPath: /images/ippon-logo.svg
githubUsername: Klopklopi
linkedinUsername: jbrthugo
---

::job-details::

- 2 years of experience
- Nantes, France
- Cloud Builder / DevOps Engineer at IPPON
- Cloud Engineer at the CCoE of Canal+
- OpenTofu Fan
- AWS Certified <logos-kubernetes />
- Kubernetes Enthusiast <logos-aws />

---
layout: about-speaker
speakerPhotoPath: /images/speaker-vivien.jpg
speakerName: Vivien MALEZE
speakerJobTitle: Platform Engineer
companyLogoPath: /images/ippon-logo.svg
githubUsername: vmaleze
linkedinUsername: vivien-maleze-1635b094
---

::job-details::

- 12 years of experience
- Bordeaux, France 🇫🇷
- Background java <logos-java />
- Developer Experience afficionados <logos-kubernetes />

---
layout: default
---

# Course of the hands on {.gradient-title}

<div class="course-grid">

<div class="course-item">

**01** 🛠️ **Dev Environment**  
Installation & Setup

</div>

<div class="course-item">

**02** 🧠 **Core Concepts**  
Understanding Observability

</div>

<div class="course-item">

**03** 🔍 **Without Observability**  
The challenges we face

</div>

<div class="course-item">

**04** ✨ **With Observability**  
How it transforms debugging

</div>

<div class="course-item">

**05** 💼 **Business Value**  
Adding custom metrics

</div>

<div class="course-item bonus">

**🎁 Bonus: Sampling**  
Optimize data collection

</div>

</div>

---
title: Core concepts
layout: image
image: /images/cogs.jpg
---

<h1 class="over-image w-145 text-white absolute-center">Core concepts of observability</h1>

---
title: Logs
layout: image
image: /images/logs.webp
---

<h1 class="over-image w-30 text-white">Logs</h1>

---
title: Metrics
layout: image
image: /images/metrics.webp
---

<h1 class="over-image w-42">Metrics</h1>

---
title: Tracing
layout: image
image: /images/distributed-tracing.png
---

<h1 class="over-image w-39">Traces</h1>

---
title: All in one
layout: default
---

# All in one - OpenTelemetry

<img src="/images/otel-logo.png" class="absolute-center"/>

---

<div class="otel-slide">

<div class="otel-columns">

<div class="otel-section otel-is">

## What is OpenTelemetry?

✅ **All-in-one** observability framework

✅ **Industry standard** for monitoring & observability

✅ **Open-source** and vendor-agnostic

✅ **Wide integration** with popular languages, frameworks & tools

</div>

<div class="otel-section otel-not">

## What is **NOT** OpenTelemetry?

❌ An observability **backend** (storage)

❌ A **frontend** for data visualization

</div>

</div>

<div class="otel-note">

💡 OpenTelemetry collects and exports telemetry data, but you still need a backend like **SigNoz**, **Jaeger**, or **Prometheus** to store and visualize it.

</div>

</div>

---
layout: image
image: /images/microservices-architecture.png
backgroundSize: 70%
---

# Application architecture and microservices interactions

---
layout: two-cols-header
---

# Let's code! 💻

::left::

<div class="content-center h-full">

## Repository to **fork**:

https://github.com/vmaleze/opentelemetry-hands-on/tree/main

</div>

::right::

![QR Code Feedback](/images/repository-vmaleze-qr-code.png)

<style>
.two-cols-header {
    grid-template-rows: 1fr 3fr;
}
h2 {
    margin-bottom: 40px;
}
</style>

---
layout: two-cols-header
---

# Let's conclude! 👋

::left::

<div class="content-center h-full">

## 🤔 Questions about the lab?

## 🙏 Don't forget the feedback!

</div>
::right::

![QR Code Feedback](/images/DevFest_Nantes_2025-feedback-qr-code.png)

<style>
.two-cols-header {
    grid-template-rows: 1fr 3fr;
}
h2 {
    margin-bottom: 40px;
}
</style>
