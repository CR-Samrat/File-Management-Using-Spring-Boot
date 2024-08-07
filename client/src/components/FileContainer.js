import axios from 'axios'
import {React, useEffect, useState} from 'react'

const FileContainer = () => {
    const[files, setFiles] = useState([])
    const[newFile, setNewFile] = useState(null)
    const[downloadSuccesser, setDownloadSuccesser] = useState(0)

    useEffect(() => {
            const fetchData = () => axios.get("http://localhost:8080/file/get")
                .then(res => {
                    console.log(res)
                    setFiles(res.data)
                }).catch(error => console.error(error))
            
            fetchData()
        },[downloadSuccesser])

    const handleDownload = (filename) => {
        axios({
            url: `http://localhost:8080/file/download/${filename}`,
            method: 'GET',
            responseType: 'blob', // Important
        })
        .then(response => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);
            document.body.appendChild(link);
            link.click();
        })
        .catch(error => console.error('Error downloading file:', error));
    };

    const handleDelete = (filename) => {
        axios.get(`http://localhost:8080/file/delete/${filename}`)
            .then(res => {
                setFiles(res.data)
                setDownloadSuccesser(downloadSuccesser+1)
            })
            .catch(error => console.error(error));
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        axios.post("http://localhost:8080/file/upload",newFile)
            .then(res => {
                console.log(res)
                if(res.status === 200) {
                    setDownloadSuccesser(downloadSuccesser+1);
                }
            })
            .catch(error => console.error(error))
    }

    const handleChange = (event) => {
        const targetFile = event.target.files[0]
        const formData = new FormData()

        formData.append("file", targetFile)

        setNewFile(formData);
    }

  return (
    <div className='fileContainer'>
        <table border={1}>
            <thead>
                <tr>
                    <th>Sl no</th>
                    <th>File name</th>
                    <th colSpan={2}>Action</th>
                </tr>
            </thead>
            <tbody>
                {   
                    files.map((eachFile,idx) => (
                        <tr key={idx+1} className='text-low'>
                            <td>{idx+1}</td>
                            <td>{eachFile}</td>
                            <td className='clickable' onClick={() => handleDownload(eachFile)}>Download</td>
                            <td className='clickable' onClick={() => handleDelete(eachFile)}>Delete</td>
                        </tr>
                    ))
                }
            </tbody>
        </table>

        <form onSubmit={(e) => handleSubmit(e)}>
            <input type="file" onChange={(e) => handleChange(e)}/>
            <input className='clickable' type="submit" value="Upload"/>
        </form>
    </div>
  )
}

export default FileContainer