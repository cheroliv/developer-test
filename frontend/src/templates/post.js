import React from "react"
import { graphql } from "gatsby"
import { MDXRenderer } from "gatsby-plugin-mdx"
import { css } from "@emotion/core"
import Layout from "../components/layout"
import ReadLink from "../components/read-link"

export const query = graphql`
  query($slug: String!) {
    mdx(frontmatter: { slug: { eq: $slug } }) {
      frontmatter {
        title
        author
      }
      body
    }
  }
`

const PostTemplate = ({ data: { mdx: post } }) => (
  <Layout>
    <h1>{post.frontmatter.title}</h1>
    <p css={css`font-size: 0.75rem;`}>
      Posted by {post.frontmatter.author}, {post.frontmatter.date}
    </p>
    <MDXRenderer>{post.body}</MDXRenderer>
    <ReadLink to="/blog">&larr; retour à tous les posts</ReadLink>
  </Layout>
)

export default PostTemplate
