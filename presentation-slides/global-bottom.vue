<!-- Check https://sli.dev/features/global-layers -->

<script setup>
// Check https://sli.dev/guide/global-context for proper types
import { useNav } from '@slidev/client';

const { currentLayout } = useNav();
</script>

<template>
  <footer
    v-if="currentLayout !== 'cover'"
    class="absolute bottom-0 left-0 right-0 p-1 flex justify-between text-sm opacity-50">
    <span></span>
    <span>OpenTelemetry Hands On</span>
    <span><SlideCurrentNo /> / <SlidesTotal /></span>
  </footer>
</template>
