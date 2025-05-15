-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 15-05-2025 a las 04:06:58
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `sgturnos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `Id_usuario` int(20) NOT NULL,
  `Primer_nombre` varchar(50) DEFAULT NULL,
  `Segundo_nombre` varchar(50) DEFAULT NULL,
  `Primer_apellido` varchar(50) DEFAULT NULL,
  `Segundo_apellido` varchar(50) DEFAULT NULL,
  `Rol` varchar(10) DEFAULT NULL,
  `Correo` varchar(50) DEFAULT NULL,
  `Contrasena` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`Id_usuario`, `Primer_nombre`, `Segundo_nombre`, `Primer_apellido`, `Segundo_apellido`, `Rol`, `Correo`, `Contrasena`) VALUES
(1101101101, 'yuliy', 'paola', 'daza', 'oviedo', 'aux01', 'yuliypdaza@paliacare.com', 'daza1101'),
(1102102102, 'melissa', 'andrea', 'solano', 'patiño', 'med03', 'MelissaSolano@paliacare.com', 'solano2102'),
(1103103103, 'angelica', 'milena', 'prada', 'cañon', 'ter04', 'angelicaprada@paliacare.com', 'prada3103'),
(1104104104, 'jesus', 'daniel', 'beltran', 'rodriguez', 'aux01', 'jesusbeltran@paliacare.com', 'beltran4104'),
(1105105105, 'carlos', 'andres', 'rodriguez', 'ochoa', 'med03', 'carlosrodriguez@paliacare.com', 'rodriguez5105'),
(1106106106, 'genny', 'carolina', 'murcia', 'vargas', 'ter04', 'gennymurcia@paliacare.com', 'murcia6106'),
(1107107107, 'jenny', 'andrea', 'martinez', 'heredia', 'enf02', 'jennyamartinez@paliacare.com', 'martinez7107'),
(1108108108, 'maria', 'camila', 'barajas', 'lopez', 'enf02', 'mariacbarajas@palicare.com', 'barajas8108'),
(1109109109, 'armando', 'stiven', 'silva', 'rodriguez', 'enf02', 'armandossilva@paliacare.com', 'silva9109'),
(1110110110, 'monica', 'patricia', 'pinilla', 'castro', 'med03', 'monicappinilla@paliacare.com', 'pinilla0110'),
(1110110111, 'Camila', 'Andrea', 'Vergara', 'Caro', 'Aux01', 'camilavergara@paliacare.com', 'vergara0111'),
(1110110112, 'Andres', 'Felipe', 'Castro', 'Polo', 'Aux01', 'andrescastro@paliacare.com', 'castro0112'),
(1110110113, 'Julia', 'Fernanda', 'Araujo', 'Henao', 'ter04', 'juliaaraujo@paliacare.com', 'araujo0113'),
(1110110114, 'Juana', 'Carolina', 'Lopez', 'Montes', 'Aux01', 'juanalopez@paliacare.com', 'lopez0114'),
(1110110115, 'Daniela', 'Carolina', 'Carvajal', 'Rio', 'Aux01', 'danielacarvajal@paliacare.com', 'carvajal0115'),
(1110110116, 'Veronica', 'Sofia', 'Cantor', 'Jimenez', 'Aux01', 'veronicacantor@paliacare.com', 'cantor0116');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`Id_usuario`),
  ADD KEY `FK_USUARIO_ROL` (`Rol`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_USUARIO_ROL` FOREIGN KEY (`Rol`) REFERENCES `rol` (`Id_rol`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
