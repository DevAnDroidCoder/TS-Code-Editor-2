// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
	title: 'TS Code Editor',
	tagline: 'TS Code Editor is an Android code editor',
	url: 'https://ts-code-editor.vercel.app/',
	baseUrl: '/',
	onBrokenLinks: 'throw',
	onBrokenMarkdownLinks: 'warn',
	favicon: 'img/logo.jpg',

	// GitHub pages deployment config.
	// If you aren't using GitHub pages, you don't need these.
	organizationName: 'TechnicalStudioDeveloper', // Usually your GitHub org/user name.
	projectName: 'TS Code Editor', // Usually your repo name.

	// Even if you don't use internalization, you can use this field to set useful
	// metadata like html lang. For example, if your site is Chinese, you may want
	// to replace "en" with "zh-Hans".
	i18n: {
		defaultLocale: 'en',
		locales: ['en'],
	},

	presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
				docs: {
					sidebarPath: require.resolve('./sidebars.js'),
					// Please change this to your repo.
					// Remove this to remove the "edit this page" links.
					editUrl: 'https://github.com/TechnicalStudioDeveloper/TS-Code-Editor',
				},
				/*blog: {
					showReadingTime: true,
					// Please change this to your repo.
					// Remove this to remove the "edit this page" links.
					editUrl: 'https://github.com/TechnicalStudioDeveloper/TS-Code-Editor',
				},*/
				theme: {
					customCss: require.resolve('./src/css/custom.css'),
				},
			}),
    ],
  ],

	themeConfig:
		/** @type {import('@docusaurus/preset-classic').ThemeConfig} */
		({
			navbar: {
				title: 'TS Code Editor',
				logo: {
					alt: 'logo',
					src: 'img/logo.jpg',
				},
				items: [
					{
						type: 'doc',
						docId: 'Introduction',
						position: 'left',
						label: 'Introduction',
          },
					{
						href: '/download',
						position: 'left',
						label: 'Download',
          },
					{
						href: 'https://github.com/TechnicalStudioDeveloper/TS-Code-Editor',
						label: 'GitHub',
						position: 'right',
          },
        ],
			},
			footer: {
				style: 'dark',
				links: [
					{
						title: 'Community',
						items: [
							{
								label: 'YouTube',
								href: 'https://youtube.com/@TechnicalStudioDeveloper',
              },
							{
								label: 'Discord',
								href: 'https://discord.gg/RM5qaZs4kd',
              },
							{
								label: 'Facebook',
								href: 'https://m.facebook.com/profile.php?id=100019763839969'
							},
            ],
          },
					{
						title: 'More',
						items: [
							/*{
								label: 'Blog',
								to: '/blog',
              },*/
							{
								label: 'GitHub',
								href: 'https://github.com/TechnicalStudioDeveloper/TS-Code-Editor',
              },
            ],
          },
        ],
				copyright: `Copyright © ${new Date().getFullYear()} TechnicalStudioDeveloper, Inc. Built with Docusaurus.`,
			},
			prism: {
				theme: lightCodeTheme,
				darkTheme: darkCodeTheme,
			},
		}),
};

module.exports = config;