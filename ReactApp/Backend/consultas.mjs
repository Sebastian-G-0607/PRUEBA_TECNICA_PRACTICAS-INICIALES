//se importa sqlite
import sqlite3 from "sqlite3";
const sqlite = sqlite3.verbose();

// Conectar a SQLite
const db = new sqlite.Database("./database.db", (err) => {
    if (err) {
        console.error("Error al abrir la base de datos", err);
    } else {
        db.run(
        "CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, edad INTEGER, direccion TEXT, telefono TEXT, fotografia TEXT)"
        );
    }  
});


//OBTIENE TODOS LOS USUARIOS DE LA BASE DE DATOS
export async function getUsers (req, res) {
    try {
        db.all("SELECT * FROM usuarios", [], (err, rows) => {
            if (err) return res.status(500).json({ error: err.message });
            res.json(rows);
        });
    } catch (error) {
        return res.status(500).json({"error": `Server error ${error}`})
    }
}

//CREA UN USUARIO EN LA BASE DE DATOS
//JSON DE ENTRADA:
/*
{
    nombre: nombre, 
    edad: edad,
    direccion: direccion,
    telefono: telefono,
    fotografia: foto
}
*/
export async function createUser (req, res) {
    try {
        //SE OBTIENE LA DATA DEL USUARIO
        const dataUser = req.body;
        db.run("INSERT INTO usuarios (nombre, edad, direccion, telefono, fotografia) VALUES (?,?,?,?,?)", [dataUser.nombre, parseInt(dataUser.edad, 10), dataUser.direccion, dataUser.telefono, dataUser.fotografia], function (err) {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ state: "created", id: this.lastID, nombre: dataUser.nombre, edad: parseInt(dataUser.edad, 10), direccion: dataUser.direccion, telefono: dataUser.telefono, fotografia: dataUser.fotografia });
        });
    } catch (error) {
        return res.status(500).json({"error": `Server error ${error}`})
    }
}

//ACTUALIZA UN USUARIO EN LA BASE DE DATOS
export async function updateUser (req, res) {
    try {
        const IDuser = req.params.id
        const dataUser = req.body
        db.run("UPDATE usuarios SET nombre=?, edad=?, direccion=?, telefono=? where id=?", [dataUser.nombre, parseInt(dataUser.edad, 10), dataUser.direccion, dataUser.telefono, IDuser], function (err) {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ message: "Usuario actualizado", });
        });
    } catch (error) {
        return res.status(500).json({"error": `Server error ${error}`})
    }
}

//ELIMINA UN USUARIO DE LA BASE DE DATOS
export async function deleteUser (req, res) {
    try {
        const dataID = req.params.id
        console.log(dataID);
        db.run("DELETE FROM usuarios WHERE id = ?", dataID, function (err) {
            if (err) return res.status(500).json({ error: err.message });
            res.json({ message: "Usuario eliminado" });
        });
    } catch (error) {
        return res.status(500).json({"error": `Server error ${error}`})
    }
}