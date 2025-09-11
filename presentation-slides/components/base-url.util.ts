export const getBaseUrl = () => {
  // Doc : https://vite.dev/guide/build.html#public-base-path
  // @ts-expect-error -- just an error in the IDE, not in the build, check https://github.com/slidevjs/slidev/issues/1151
  const currentBaseUrl: string = import.meta.env.BASE_URL;
  return currentBaseUrl.slice(0, -1); // remove trailing slash
};
