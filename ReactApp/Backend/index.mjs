//Archivo principal del backend
import {app} from './app.mjs'

//Puerto en el que se levantará el servidor
const port = 4000;

app.listen(port, () => console.log(`Servidor ejecutándose en el puerto ${port}`));