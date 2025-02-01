import { useState, useEffect } from 'react'
import axios from 'axios'
import { Modal, Button } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css"; // Importa Bootstrap

import './App.css'


function App() {
  //VARIABLES QUE VAN A CONTENER LOS INPUTS DEL USUARIO
  const [ newName, setNewName ] = useState('')
  const [ newAge, setNewAge ] = useState('')
  const [ newAddress, setNewAddress ] = useState('')
  const [ newPhone, setNewPhone ] = useState('')
  const [ base64, setBase64 ] = useState('')

  //ARRAY QUE CONTIENE LA LISTA DE USUARIOS EN EL SISTEMA
  const [ users, setUsers ] = useState([])

  //BANDERA PARA SABER CUANDO CAMBIE EL ESTADO DE LA PÁGINA
  const [ page, setPage ] = useState(true)

  //VARIABLE QUE CONTIENE EL USUAIRO SELECCIONADO PARA ACTIVAR EL MODAL
  const [ selectedUser, setSelectedUser ] = useState(null)

  //USE EFFECT PARA OBTENER LOS DATOS DE LA DATABASE
  useEffect(() => {
    //SE ENVÍA LA PETICIÓN AL BACKEND
    axios.get('http://localhost:4000/users')
      .then(response => {setUsers(response.data); console.log(response.data)})
  }, [page])

  //FUNCION QUE MANEJA LA CARGA DE IMAGENES
  const handleFileUpload = (event) => {
    const file = event.target.files[0]; // Obtiene el primer archivo seleccionado
    if (file) {
      const reader = new FileReader(); // Crea un FileReader
      reader.onload = (e) => {
        setBase64(e.target.result); // Guarda el Base64 en el estado
      };
      reader.readAsDataURL(file); // Convierte la imagen a Base64
    }
  }

  //FUNCIÓN QUE MANEJA EL ENVÍO DEL FORMULARIO
  const handleForm = (e) => {
    e.preventDefault();

    //SE CREA EL JSON QUE SE VA A ENVIAR AL BACK
    const nuevoUsuario = {
      nombre: newName,
      edad: newAge,
      direccion: newAddress,
      telefono: newPhone,
      fotografia: base64
    }

    //SE ENVÍA LA PETICIÓN AL BACK
    axios.post('http://localhost:4000/newUser', nuevoUsuario)
      .then(response => {
        console.log(response.data);
        setPage(!page)
        setNewName('')
        setNewAddress('')
        setNewAge('')
        setNewPhone('')
        setBase64('')
      })
      .catch(error => console.log(error))
  }

  //FUNCIÓN PARA ESTABLECER EL CURSO SELECCIONADO Y PODER ACTUALIZARLO:
  const setSUser = (user) => {
    setSelectedUser(user);
    setNewName(user.nombre);
    setNewAge(user.edad)
    setNewAddress(user.direccion)
    setNewPhone(user.telefono)
  }

  //FUNCIÓN PARA ACTUALIZAR UN REGISTRO:
  const updateUser = (idUser) => {
    //SE CREA EL JSON QUE SE VA A ENVIAR AL BACK
    const usuarioActualizado = {
      nombre: newName,
      edad: newAge,
      direccion: newAddress,
      telefono: newPhone
    }
    //SE ENVÍA LA PETICIÓN AL BACK
    axios.put(`http://localhost:4000/updateUser/${idUser}`, usuarioActualizado)
    .then(response => {
      console.log(response)
      alert("Usuario actualizado con éxito")
      setSelectedUser(null)
      setPage(!page)
      setNewName('')
      setNewAddress('')
      setNewAge('')
      setNewPhone('')
      setBase64('')
    })
  }

  //FUNCIÓN QUE ELIMINA UN REGISTRO
  const deleteUser = (id_usuario) => {
    axios.delete(`http://localhost:4000/deleteUser/${id_usuario}`)
      .then(response => {
        console.log(response)
        setPage(!page)
      })
  }
  
  return (
    <>
      <div className="container">
          <h1>Gestión de Usuarios</h1>
          <form id="userForm" onSubmit={handleForm}>
              <label htmlFor="name">Nombre:</label>
              <input type="text" id="name" name="name" value={newName} onChange={(e) => setNewName(e.target.value)} required />

              <label htmlFor="age">Edad:</label>
              <input type="number" id="age" name="age"  value={newAge} onChange={(e) => setNewAge(e.target.value)} required />

              <label htmlFor="address">Dirección:</label>
              <input type="text" id="address" name="address" value={newAddress} onChange={(e) => setNewAddress(e.target.value)} required />

              <label htmlFor="phone">Teléfono:</label>
              <input type="number" id="phone" name="phone" value={newPhone} onChange={(e) => setNewPhone(e.target.value)} required /> 

              <label htmlFor="photo">Fotografía:</label>
              <input type="file" id="photo" name="photo" accept="image/*" onChange={handleFileUpload} required /> 
              <button type="submit">Agregar Usuario</button>
          </form>
          <table>
              <thead>
                  <tr>
                      <th>Nombre</th>
                      <th>Edad</th>
                      <th>Dirección</th>
                      <th>Teléfono</th>
                      <th>Fotografía</th>
                      <th>Acciones</th>
                  </tr>
              </thead>
              <tbody id="userTableBody">
                {users.map(user =>
                  <tr key={user.id}>
                      <td>{user.nombre}</td>
                      <td>{user.edad}</td>
                      <td>{user.direccion}</td>
                      <td>{user.telefono}</td>
                      <td><img src={user.fotografia} alt="Foto de usuario" className="user-photo" /></td>
                      <td className="action-buttons">
                        <button className="update-btn" onClick={() => setSUser(user)}>Actualizar</button>
                        <button className="delete-btn" onClick={() => deleteUser(user.id)}>Eliminar</button>
                      </td>
                  </tr>
                )}
              </tbody>
          </table>
      </div>

      {selectedUser && (
        <Modal show={true} onHide={() => setSUser(null)}>
            <Modal.Header>
                <Modal.Title>Editar curso</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                    <h2 style={{ margin: '0 10px 0 0', fontSize: '22px' }}>Nombre</h2>
                    <input 
                        type="text" 
                        value={newName}
                        style={{ flex: 1, padding: '5px', border: '1px solid #d3d3d3', borderRadius: '4px' }}
                        onChange={(e) => setNewName(e.target.value)}
                    />
                </div>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                    <h2 style={{ margin: '0 10px 0 0', fontSize: '22px' }}>Edad</h2>
                    <input 
                        type="text" 
                        placeholder='Edad del usuario'
                        style={{ flex: 1, padding: '5px', border: '1px solid #d3d3d3', borderRadius: '4px' }} 
                        value={newAge}
                        onChange={(e) => setNewAge(e.target.value)}
                    />
                </div>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                    <h2 style={{ margin: '0 10px 0 0', fontSize: '22px' }}>Dirección</h2>
                    <input 
                        type="text" 
                        value={newAddress}
                        onChange={(e) => setNewAddress(e.target.value)}
                        style={{ flex: 1, padding: '5px', border: '1px solid #d3d3d3', borderRadius: '4px' }} 
                    />
                </div>
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '10px' }}>
                    <h2 style={{ margin: '0 10px 0 0', fontSize: '22px' }}>Teléfono</h2>
                    <input 
                        type="text" 
                        value={newPhone}
                        onChange={(e) => setNewPhone(e.target.value)}
                        style={{ flex: 1, padding: '5px', border: '1px solid #d3d3d3', borderRadius: '4px' }} 
                    />
                </div>
            </Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={() => setSUser(null)}>
                    Cerrar
                </Button>
                <Button variant="primary" onClick={() => {updateUser(selectedUser.id); }}>
                    Guardar cambios
                </Button>
            </Modal.Footer>
        </Modal>
      )}

    </>
  )
}

export default App
