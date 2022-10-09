import React from "react";
import axios from "axios";

const api_url= "http://localhost:8080/api/give-me-the-odds";

export default function Empire() {
  const [uploadFile, setUploadFile] = React.useState();

  const submitForm = (event) => {
    event.preventDefault();

    const dataArray = new FormData();
    dataArray.append("uploadFile", uploadFile);

    axios
        .post(
           api_url,
            dataArray,
            { headers: {"Content-Type": "multipart/form-data"}}
        )
        .then((response) => {
            // successfully uploaded response
          })
          .catch((error) => {
            // error response
          });
  };

  return (
    <div>
      <form onSubmit={submitForm}>
        <input type="file" onChange={(e) => setUploadFile(e.target.files)} />
        <br />
        <input type="submit" />
      </form>
    </div>
  );
}