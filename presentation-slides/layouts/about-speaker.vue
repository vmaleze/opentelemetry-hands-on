<script setup lang="ts">
defineProps({
  speakerPhotoPath: { type: String, required: true },
  speakerName: { type: String, required: true },
  speakerJobTitle: { type: String, required: true },
  companyLogoPath: { type: String, required: true },
  githubUsername: { type: String, required: false },
  linkedinUsername: { type: String, required: false },
});
</script>

<template>
  <div class="slidev-layout default">
    <div class="about-speaker">
      <img :src="speakerPhotoPath" alt="Speaker Photo" class="speaker-photo" />
      <div class="speaker-info">
        <h1 class="speaker-name">{{ speakerName }}</h1>
        <h2 class="speaker-title">{{ speakerJobTitle }}</h2>
      </div>
    </div>
    <div class="about-job">
      <div class="job-details-slot">
        <slot name="job-details" />
      </div>
      <img :src="companyLogoPath" alt="Company Logo" class="company-logo" />
    </div>
    <NetworkLinks :githubUsername :linkedinUsername />
  </div>
</template>

<style scoped>
.about-speaker,
.about-job {
  display: flex;
  align-items: center;
  gap: 3em;
}
.about-job {
  justify-content: space-between;
}

.speaker-photo {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  box-shadow: 0 0 8px var(--ippon-color-jaune);
}
.speaker-name {
  font-size: 2.5rem;
}
.speaker-title {
  margin-top: 1em;
  font-size: 2rem;
}

.company-logo {
  width: 200px;
  height: 200px;
}
.job-details-slot {
  margin-top: 1em;
  font-size: 1.2rem;
}
</style>
