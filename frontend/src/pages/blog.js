import React from "react"
import Layout from "../components/layout"
import PostPreview from "../components/post-preview"
import usePosts from "../hooks/use-posts"


const Blog = () =>
  <Layout>
    {usePosts().map(post => <PostPreview key={post.slug} post={post} />)}
  </Layout>
export default Blog