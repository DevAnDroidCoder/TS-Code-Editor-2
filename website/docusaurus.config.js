// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

/** @type {import('@docusaurus/types').Config} */
const config = {
	title: 'TS Code Editor',
	tagline: 'TS Code Editor is a Android code editor',
	url: 'https://html-creator-master-kpp1akza8-technicalstudiodevelopers.vercel.app/',
	baseUrl: '/',
	onBrokenLinks: 'throw',
	onBrokenMarkdownLinks: 'warn',
	favicon: 'img/logo.jpg',

	// GitHub pages deployment config.
	// If you aren't using GitHub pages, you don't need these.
	organizationName: 'TechnicalStudioDeveloper', // Usually your GitHub org/user name.
	projectName: 'Website-Creator', // Usually your repo name.

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
					editUrl: 'https://github.com/TechnicalStudioDevelopers/Website-Creator',
				},
				blog: {
					showReadingTime: true,
					// Please change this to your repo.
					// Remove this to remove the "edit this page" links.
					editUrl: 'https://github.com/TechnicalStudioDevelopers/Website-Creator',
				},
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
				title: 'Website-Creator',
				logo: {
					alt: 'My Site Logo',
					src: 'img/logo.jpg',
				},
				items: [
					{
						type: 'doc',
						docId: 'doc1/intro',
						position: 'left',
						label: 'Tutorial',
          },
          {
          	type:'doc',
          	docId:'doc2/download',
          	position:'left',
          	label:'Download',
          },
					{
						to: '/blog',
						label: 'Blog',
						position: 'left'
					},
					{
						href: 'https://github.com/TechnicalStudioDevelopers/Website-Creator',
						label: 'GitHub',
						position: 'right',
          },
        ],
			},
			footer: {
				style: 'dark',
				links: [
					{
						title: 'Docs',
						items: [
							{
								label: 'Tutorial',
								to: '/docs/doc1/intro',
              },
            ],
          },
					{
						title: 'Community',
						items: [
							{
								label: 'YouTube',
								href: 'https://youtube.com/@TechnicalStudioDeveloper',
              },
							{
								label: 'Discord',
								href: 'https://discord.gg/xG7Y6buT',
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
							{
								label: 'Blog',
								to: '/blog',
              },
							{
								label: 'GitHub',
								href: 'https://github.com/TechnicalStudioDevelopers/Website-Creator',
              },
            ],
          },
        ],
				copyright: `Copyright © ${new Date().getFullYear()} TS Code Editor, Inc. Built with Docusaurus.`,
			},
			prism: {
				theme: lightCodeTheme,
				darkTheme: darkCodeTheme,
			},
		}),
};

module.exports = config;