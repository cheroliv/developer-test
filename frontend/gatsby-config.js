module.exports = {
    flags: {
        PRESERVE_WEBPACK_CACHE: true,
    },
    siteMetadata: {
        title: "Computer Ship UI",
        description: "Millenium Falcon, Computer Ship user interface",
    },
    plugins: [
        "gatsby-plugin-emotion",
        "gatsby-plugin-react-helmet",
        'gatsby-transformer-sharp',
        'gatsby-plugin-sharp',
        {
            resolve: 'gatsby-plugin-mdx',
            options: {
                defaultLayouts: {
                    default: require.resolve('./src/components/layout.js'),
                },
                gatsbyRemarkPlugins: [{resolve: 'gatsby-remark-images'}],
                plugins: [{resolve: 'gatsby-remark-images'}],
            },
        },
        {
            resolve: 'gatsby-source-filesystem',
            options: {
                name: 'images',
                path: 'images',
            },
        },
        {
            resolve: 'gatsby-source-filesystem',
            options: {
                name: 'posts',
                path: 'posts',
            },
        },
        {
            resolve: 'gatsby-source-filesystem',
            options: {
                name: 'realisations',
                path: 'realisations',
            },
        },
        {
            resolve: 'gatsby-plugin-webpack-bundle-analyzer',
            options: {
                production: true,
                disable: !process.env.ANALYZE_BUNDLE_SIZE,
                generateStatsFile: true,
                analyzerMode: 'static',
            },
        },
    ],
};
