import { graphql, useStaticQuery } from "gatsby"

const useSiteMetadata = () => {
  const { site: { siteMetadata } } = useStaticQuery(graphql`
    query {
        site {
            siteMetadata {
                title
                description
            }
        }
    }`)
  return siteMetadata
}
export default useSiteMetadata