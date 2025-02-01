//instancia del framework express
import express from 'express';

//Objeto para hacer rutas
export const router = express.Router();

//Importando los controladores
import { getUsers, createUser, updateUser, deleteUser } from './consultas.mjs';

//Creando los endpoints

router.get('/users', getUsers)
router.post('/newUser', createUser)
router.put('/updateUser/:id', updateUser)
router.delete('/deleteUser/:id', deleteUser)