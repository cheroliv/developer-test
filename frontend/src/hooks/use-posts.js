import { graphql, useStaticQuery } from "gatsby"

const usePosts = () => useStaticQuery(graphql`
  query{
    allMdx (
      filter: {frontmatter:{type: {eq:"post"}}}
        sort: { 
          fields: [frontmatter___date]
          order: DESC
        }
    ) {
      nodes {
        frontmatter {
          title
          author
          slug
          date
          type
          image {
            sharp: childImageSharp {
              fluid(
                maxWidth: 100
                maxHeight: 100
              ) { ...GatsbyImageSharpFluid_withWebp}
            }
          }          
        }
        excerpt
      }
    }
  }`).allMdx.nodes.map(post => ({
  title: post.frontmatter.title,
  author: post.frontmatter.author,
  slug: post.frontmatter.slug,
  date: post.frontmatter.date,
  image: post.frontmatter.image,
  excerpt: post.excerpt
}))

export default usePosts