export type Network = {
  /**
   * Base URL of the network
   */
  baseUrl: string;

  /**
   * Path to the icon image (prefer SVG).
   * It must start with a slash. The file must be in the `public` folder.
   */
  iconPath: string;
};

/**
 * Available social networks for the SocialLinks component.
 * The key of the record is the network name.
 */
export const networks: Record<string, Network> = {
  github: {
    baseUrl: 'https://github.com',
    iconPath: '/images/github-logo.svg',
  },
  linkedin: {
    baseUrl: 'https://linkedin.com/in',
    iconPath: '/images/linkedin-logo.svg',
  },
};
