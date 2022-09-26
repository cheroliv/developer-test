import React from "react"
import styled from "@emotion/styled"
import { graphql, useStaticQuery } from "gatsby"
import BackgroundImage from "gatsby-background-image"

const ImageBackground = styled(BackgroundImage)`
  background-position: top 20% center;
  background-size: cover;
  height: 30vh;
  /* override the default margin for sibling elements  */
  + * { margin-top: 0;}`

const TextBox = styled("div")`
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: flex-end;
  padding: 0 calc((100vw - 550px) / 2) 2rem;
  width: 100%;
  h1 {
    text-shadow: 1px 1px 3px #eeddff66;
    font-size: 2.25rem;
  }
  p, a {
    color: #222;
    margin-top: 0;
  }

  a { margin-top: 0.5rem;}`

const Hero = () => {
  const { image } = useStaticQuery(graphql`
    query {
      image: file(relativePath: { eq: "bill-oxford.jpg" }) {
        sharp: childImageSharp {
          fluid {
            ...GatsbyImageSharpFluid_withWebp
          }
        }
      }
    }`)

  return <ImageBackground Tag="section"
                          fluid={image.sharp.fluid}
                          fadeIn="soft">
    <TextBox />
  </ImageBackground>
}

export default Hero
