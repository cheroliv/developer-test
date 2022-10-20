import React from "react";
import axios from "axios";


const api_url = "http://localhost:8080/api/give-me-the-odds";

export default function Empire() {
    const [uploadFile, setUploadFile] = React.useState();

    const submitForm = (event) => {
        event.preventDefault();
        const reader = new FileReader();
        reader.addEventListener('load', (event) => {
            axios.post(api_url,
                       reader.result,
                        { headers: {"Content-Type": "application/json"}})
                .then((response) => {
                    console.log(response.data);
                    //TODO: display odds
                    })
                .catch((error) => {console.log(error.response.data.message); });
        });
        reader.readAsText(uploadFile.item(0));
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
