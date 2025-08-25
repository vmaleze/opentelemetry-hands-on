<script setup lang="ts">
import { networks } from './NetworkLinks.model';

const props = defineProps({
  githubUsername: String,
  linkedinUsername: String,
});

const isNetworkUsernameDefined = (network: string) => {
  return props[`${network}Username`] !== undefined;
};

const getUsernameByNetwork = (network: string) => {
  return props[`${network}Username`];
};
</script>

<template>
  <div>
    <template v-for="network in Object.keys(networks)">
      <a
        v-if="isNetworkUsernameDefined(network)"
        :href="`${networks[network].baseUrl}/${getUsernameByNetwork(network)}`"
        target="_blank"
        rel="noopener noreferrer">
        <img :src="`${networks[network].iconPath}`" :alt="`${network} icon`" class="icon" />
        <span>{{ getUsernameByNetwork(network) }}</span>
      </a>
    </template>
  </div>
</template>

<style scoped>
div {
  display: flex;
  gap: 4rem;
  justify-content: center;
  padding: 12px;
}
a {
  display: inline-flex;
  border: none;
}
.icon {
  width: 1rem;
  margin-right: 0.5rem;
}
</style>
