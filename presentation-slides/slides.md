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

# random image from a curated Unsplash collection by Anthony
# like them? see https://unsplash.com/collections/94734566/slidev
background: https://images.unsplash.com/photo-1484503793037-5c9644d6a80a
class: text-center # apply unocss classes to the current slide
layout: cover
---

# OpenTelemetry Hands On

## 2h chrono pour traquer tout ce qui bouge dans ton backend avec OpenTelemetry

Durée : 2 heures

![Logo IPPON](/images/ippon-logo.svg)

---
layout: about-speaker
speakerPhotoPath: /images/speaker-sebastien-oddo.jpg
speakerName: Sébastien Oddo
speakerJobTitle: Tech-lead Frontend
companyLogoPath: /images/ippon-logo.svg
githubUsername: Sebi11
linkedinUsername: sébastien-oddo
---

::job-details::

- 9 years of experience
- Vannes, Brittany, France
- Technical Manager at IPPON
- Tech-Lead Consultant at JCDecaux
- Senior Angular Developer <logos-angular-icon />

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

# Course of the hands on

- Dev environment installation
- Core concepts
- What happens when you need to work without observability ?
- And with it ?
- Adding business value
- Bonus => Sampling data

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

# What is OpenTelemetry?

- All in one observability framework!
- Default standard nowadays in the observability and monitoring world
- Open-source and vendor-agnostic
- Integrated with the most popular langages, frameworks and tools

\_\_

# What is not OpenTelemetry?

- An observability backend (storage)
- A frontend (vizualization) of telemetry data

---
layout: image
image: /images/microservices-architecture.png
backgroundSize: 70%
---

# Application architecture and microservices interactions

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
