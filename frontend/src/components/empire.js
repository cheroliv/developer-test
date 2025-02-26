import React, { useState } from "react";
import axios from "axios";


const api_url = "http://localhost:8080/api/give-me-the-odds";
const postHeaders = { headers: {"Content-Type": "application/json"}}

export default function Empire() {
    const [uploadFile, setUploadFile] = useState();
    const [odds,setOdds] = useState();

    const submitEmpire = (event) => {
        event.preventDefault();
        const reader = new FileReader();
        reader.addEventListener('load', (event) => {
            axios.post(api_url, reader.result, postHeaders)
                .then((response) => { setOdds(response.data);})
                .catch((error) => { console.log(error.response.data.message);});
        });
        reader.readAsText(uploadFile.item(0));
    };

    return (<div>
        <form onSubmit = { submitEmpire } >
            <input type = "file"
                   name = "empire"
                   onChange = {(e) => setUploadFile(e.target.files)}/>
            <br/>
            <input type = "submit" / >
        </form>
	<p>
        {odds == 0 && <>probability of success: 0 %</>}
        {odds == 1 && <>probability of success: 100 %</>}
        {odds < 1 && odds >0 && <>probability of success: {odds*100} %</>}
	</p>
    </div>);
}