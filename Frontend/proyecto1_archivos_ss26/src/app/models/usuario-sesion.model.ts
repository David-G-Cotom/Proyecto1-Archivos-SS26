import { NombreRol } from "./nombre-rol";

export interface UsuarioSesion {
    username: string;
    nombreCompleto: string;
    rol: NombreRol;
}
