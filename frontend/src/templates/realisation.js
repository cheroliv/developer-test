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
        type
      }
      body
    }
  }`

const RealisationTemplate = ({ data: { mdx: realisation } }) => {
  if (realisation.frontmatter.type.toString() === "realisation")
    return <Layout>
      <h1>{realisation.frontmatter.title}</h1>
      <p css={css`font-size: 0.75rem;`}>
        Posted by {realisation.frontmatter.author}, {realisation.frontmatter.date}
      </p>
      <MDXRenderer>{realisation.body}</MDXRenderer>
      <ReadLink to="/portfolio">&larr; retour à toutes les réalisations</ReadLink>
    </Layout>
}

export default RealisationTemplate
