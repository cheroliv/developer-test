import React from "react";
import axios from "axios";

const api_url = "http://localhost:8080/api/give-me-the-odds";
const empire = "empire"
export default function Empire() {
    const [uploadFile, setUploadFile] = React.useState();

    const submitForm = (event) => {
        event.preventDefault();

        const dataArray = new FormData();
        dataArray.append(empire, uploadFile);

        axios
            .post(api_url, dataArray)
            .then((response) => {
                // successfully uploaded response
                console.log(empire)
                console.log(dataArray[empire])
            })
            .catch((error) => {
                // error response
                console.log(dataArray[empire])
            });
    };

    return ( <
        div >
        <
        form onSubmit = { submitForm } >
        <
        input type = "file"
        name = "empire"
        onChange = {
            (e) => setUploadFile(e.target.files) }
        /> <
        br / >
        <
        input type = "submit" / >
        <
        /form> <
        /div>
    );
}