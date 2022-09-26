exports.createPages = async ({actions, graphql, reporter}) => {
    const result = await graphql(`
    query {
      allMdx {
        nodes {
          frontmatter {
            slug
            type
          }
        }
      }
    }`);

    if (result.errors) {
        reporter.panic('failed to create posts', result.errors);
    }


    result.data.allMdx.nodes.forEach(item => {
        if (item.frontmatter.type.toString() === "post")
            actions.createPage({
                path: `blog/${item.frontmatter.slug}`,
                component: require.resolve('./src/templates/post.js'),
                context: {
                    slug: item.frontmatter.slug,
                },
            });
        if (item.frontmatter.type.toString() === "realisation")
            actions.createPage({
                path: `portfolio/${item.frontmatter.slug}`,
                component: require.resolve('./src/templates/realisation.js'),
                context: {
                    slug: item.frontmatter.slug,
                },
            });
    });
};
