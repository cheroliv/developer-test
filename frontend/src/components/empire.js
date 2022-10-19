import React from "react";
import axios from "axios";


const api_url = "http://localhost:8080/api/give-me-the-odds";

export default function Empire() {
    const [uploadFile, setUploadFile] = React.useState();

    const submitForm = (event) => {
        event.preventDefault();

        const empire = `{
            "countdown": 7,
            "bounty_hunters": [
              {
                "planet": "Hoth",
                "day": 6
              },
              {
                "planet": "Hoth",
                "day": 7
              },
              {
                "planet": "Hoth",
                "day": 8
              }
            ]
          }`

        //        const empire = fs.readFileSync(uploadFile, {encoding: 'utf-8'})
        console.log(empire)

        axios
            .post(api_url, empire)
            .then((response) => {
                //                 successfully uploaded response
                console.log(empire)
            })
            .catch((error) => {
                //                 error response
                console.log(empire)
            });
    };

    return (<div>
        <form onSubmit = { submitForm } >
        <input type = "file"
        name = "empire"
        onChange = {(e) => setUploadFile(e.target.files)}/>
        <br/>
        <input type = "submit" / >
        </form>
        </div>);
}