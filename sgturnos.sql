-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-05-2025 a las 21:06:25
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
-- Estructura de tabla para la tabla `asignacion_turno`
--

CREATE TABLE `asignacion_turno` (
  `Id_turno` int(20) NOT NULL,
  `Id_empleado` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asignacion_turno`
--

INSERT INTO `asignacion_turno` (`Id_turno`, `Id_empleado`) VALUES
(1, '1105105105'),
(2, '1106106106'),
(3, '1107107107'),
(4, '1108108108'),
(5, '1109109109'),
(6, '1102102102');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `capacitacion`
--

CREATE TABLE `capacitacion` (
  `Id_capacitacion` int(11) NOT NULL,
  `Id_tema` varchar(30) NOT NULL,
  `Fecha` date NOT NULL,
  `Id_estado_cap` varchar(30) NOT NULL,
  `Id_empleado` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `departamento`
--

CREATE TABLE `departamento` (
  `Id_departamento` varchar(10) NOT NULL,
  `Departamento` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `departamento`
--

INSERT INTO `departamento` (`Id_departamento`, `Departamento`) VALUES
('D001', 'Recursos Humanos'),
('D002', 'Enfermería'),
('D003', 'Administración'),
('enfer01', NULL),
('eqreh03', NULL),
('medic02', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `Id_empleado` varchar(20) NOT NULL,
  `Id_rol` varchar(10) NOT NULL,
  `id_usuario` int(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`Id_empleado`, `Id_rol`, `id_usuario`) VALUES
('1101101101', 'aux01', 1101101101),
('1102102102', 'med03', 1102102102),
('1103103103', 'ter04', 1103103103),
('1104104104', 'aux01', 1104104104),
('1105105105', 'med03', 1105105105),
('1106106106', 'ter04', 1106106106),
('1107107107', 'enf02', 1107107107),
('1108108108', 'enf02', 1108108108),
('1109109109', 'enf02', 1109109109),
('1110110110', 'med03', 1110110110);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_capacitacion`
--

CREATE TABLE `estado_capacitacion` (
  `Id_estado_cap` varchar(30) NOT NULL,
  `Estado_de_cap` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estado_capacitacion`
--

INSERT INTO `estado_capacitacion` (`Id_estado_cap`, `Estado_de_cap`) VALUES
('e_cap01', 'Programada'),
('e_cap02', 'Cancelada'),
('e_cap03', 'Realizada');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado_novedad`
--

CREATE TABLE `estado_novedad` (
  `Id_Estado_nov` varchar(20) NOT NULL,
  `Estado_nov` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estado_novedad`
--

INSERT INTO `estado_novedad` (`Id_Estado_nov`, `Estado_nov`) VALUES
('01_APRO', 'APROBADO'),
('02_RECH', 'RECHAZADO'),
('03_PEND', 'PENDIENTE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `horario`
--

CREATE TABLE `horario` (
  `id_horario` varchar(10) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `horario`
--

INSERT INTO `horario` (`id_horario`, `hora_inicio`, `hora_fin`) VALUES
('h01', '07:00:00', '19:00:00'),
('h02', '19:00:00', '07:00:00'),
('h03', '07:00:00', '11:00:00');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `novedades`
--

CREATE TABLE `novedades` (
  `Id_novedad` varchar(20) NOT NULL,
  `Id_tipo_novedad` varchar(20) NOT NULL,
  `Fecha_de_reporte` varchar(20) NOT NULL,
  `id_estado_nov` varchar(20) NOT NULL,
  `Id_empleado` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `novedades`
--

INSERT INTO `novedades` (`Id_novedad`, `Id_tipo_novedad`, `Fecha_de_reporte`, `id_estado_nov`, `Id_empleado`) VALUES
('1', 'vac01', '2025-03-16', '01_APRO', '1101101101'),
('2', 'cal02', '2025-03-16', '02_RECH', '1102102102'),
('3', 'inc03', '2025-03-17', '01_APRO', '1103103103'),
('4', 'cap04', '2025-03-22', '03_PEND', '1104104104'),
('5', 'ext05', '2025-03-15', '01_APRO', '1107107107'),
('6', 'cam_t06', '2025-03-16', '01_APRO', '1108108108'),
('7', 'per07', '2025-03-17', '02_RECH', '1109109109'),
('8', 'cam_h08', '2025-03-22', '01_APRO', '1110110110');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `Id_rol` varchar(10) NOT NULL,
  `Rol` varchar(50) NOT NULL,
  `Id_departamento` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`Id_rol`, `Rol`, `Id_departamento`) VALUES
('aux01', '', 'enfer01'),
('enf02', '', 'enfer01'),
('med03', '', 'medic02'),
('ter04', '', 'eqreh03');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tema_capacitacion`
--

CREATE TABLE `tema_capacitacion` (
  `Id_tema` varchar(30) NOT NULL,
  `Tema` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tema_capacitacion`
--

INSERT INTO `tema_capacitacion` (`Id_tema`, `Tema`) VALUES
('tcap01', 'Transferencias'),
('tcap02', 'Baño'),
('tcap03', 'Administración de Medicamentos'),
('tcap04', 'Manejo Cortopunzantes'),
('tcap05', 'Manejo de Residuos');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipo_novedad`
--

CREATE TABLE `tipo_novedad` (
  `Id_tipo_novedad` varchar(20) NOT NULL,
  `Novedad` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tipo_novedad`
--

INSERT INTO `tipo_novedad` (`Id_tipo_novedad`, `Novedad`) VALUES
('cal02', ''),
('cam_h08', ''),
('cam_t06', ''),
('cap04', ''),
('ext05', ''),
('inc03', ''),
('per07', ''),
('vac01', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `turno`
--

CREATE TABLE `turno` (
  `Id_turno` int(20) NOT NULL,
  `Fecha_ini` date NOT NULL,
  `Fecha_fin` date NOT NULL,
  `Id_horario` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turno`
--

INSERT INTO `turno` (`Id_turno`, `Fecha_ini`, `Fecha_fin`, `Id_horario`) VALUES
(1, '2025-04-15', '2025-04-15', 'h01'),
(2, '2025-04-16', '2025-04-17', 'h02'),
(3, '2025-04-17', '2025-04-17', 'h01'),
(4, '2025-04-16', '2025-04-17', 'h02'),
(5, '2025-04-19', '2025-04-19', 'h01'),
(6, '2025-04-16', '2025-04-17', 'h02');

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
(1110110116, 'Veronica', 'Sofia', 'Cantor', 'Jimenez', 'Aux01', 'veronicacantor@paliacare.com', 'cantor0116'),
(1110110117, 'Carla', 'Antonia', 'Munoz', 'Alvarez', 'Aux01', 'carlamunoz@paliacare.com', 'munoz0117');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asignacion_turno`
--
ALTER TABLE `asignacion_turno`
  ADD PRIMARY KEY (`Id_turno`,`Id_empleado`),
  ADD KEY `Id_empleado` (`Id_empleado`);

--
-- Indices de la tabla `capacitacion`
--
ALTER TABLE `capacitacion`
  ADD PRIMARY KEY (`Id_capacitacion`),
  ADD KEY `Id_tema` (`Id_tema`),
  ADD KEY `Id_estado_cap` (`Id_estado_cap`),
  ADD KEY `Id_empleado` (`Id_empleado`);

--
-- Indices de la tabla `departamento`
--
ALTER TABLE `departamento`
  ADD PRIMARY KEY (`Id_departamento`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`Id_empleado`),
  ADD KEY `FK_EMPLEADO_ROL` (`Id_rol`),
  ADD KEY `FK_EMPLEADO_USUARIO` (`id_usuario`);

--
-- Indices de la tabla `estado_capacitacion`
--
ALTER TABLE `estado_capacitacion`
  ADD PRIMARY KEY (`Id_estado_cap`);

--
-- Indices de la tabla `estado_novedad`
--
ALTER TABLE `estado_novedad`
  ADD PRIMARY KEY (`Id_Estado_nov`);

--
-- Indices de la tabla `horario`
--
ALTER TABLE `horario`
  ADD PRIMARY KEY (`id_horario`);

--
-- Indices de la tabla `novedades`
--
ALTER TABLE `novedades`
  ADD PRIMARY KEY (`Id_novedad`),
  ADD KEY `FK_NOVEDADES_TIPO_NOVEDAD` (`Id_tipo_novedad`),
  ADD KEY `FK_NOVEDADES_ESTADO_NOV` (`id_estado_nov`),
  ADD KEY `fk_novedades_empleado` (`Id_empleado`);

--
-- Indices de la tabla `rol`
--
ALTER TABLE `rol`
  ADD PRIMARY KEY (`Id_rol`),
  ADD KEY `FK_ROL_DEPARTAMENTO` (`Id_departamento`);

--
-- Indices de la tabla `tema_capacitacion`
--
ALTER TABLE `tema_capacitacion`
  ADD PRIMARY KEY (`Id_tema`);

--
-- Indices de la tabla `tipo_novedad`
--
ALTER TABLE `tipo_novedad`
  ADD PRIMARY KEY (`Id_tipo_novedad`);

--
-- Indices de la tabla `turno`
--
ALTER TABLE `turno`
  ADD PRIMARY KEY (`Id_turno`),
  ADD KEY `FK_TURNO_HORARIO` (`Id_horario`);

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
-- Filtros para la tabla `asignacion_turno`
--
ALTER TABLE `asignacion_turno`
  ADD CONSTRAINT `asignacion_turno_ibfk_1` FOREIGN KEY (`Id_turno`) REFERENCES `turno` (`Id_turno`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `asignacion_turno_ibfk_2` FOREIGN KEY (`Id_empleado`) REFERENCES `empleado` (`Id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `capacitacion`
--
ALTER TABLE `capacitacion`
  ADD CONSTRAINT `capacitacion_ibfk_1` FOREIGN KEY (`Id_tema`) REFERENCES `tema_capacitacion` (`Id_tema`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `capacitacion_ibfk_2` FOREIGN KEY (`Id_estado_cap`) REFERENCES `estado_capacitacion` (`Id_estado_cap`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `capacitacion_ibfk_3` FOREIGN KEY (`Id_empleado`) REFERENCES `empleado` (`Id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `FK_EMPLEADO_ROL` FOREIGN KEY (`Id_rol`) REFERENCES `rol` (`Id_rol`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_EMPLEADO_USUARIO` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`Id_usuario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `novedades`
--
ALTER TABLE `novedades`
  ADD CONSTRAINT `FK_NOVEDADES_ESTADO_NOV` FOREIGN KEY (`id_estado_nov`) REFERENCES `estado_novedad` (`Id_Estado_nov`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_NOVEDADES_TIPO_NOVEDAD` FOREIGN KEY (`Id_tipo_novedad`) REFERENCES `tipo_novedad` (`Id_tipo_novedad`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_novedades_empleado` FOREIGN KEY (`Id_empleado`) REFERENCES `empleado` (`Id_empleado`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `rol`
--
ALTER TABLE `rol`
  ADD CONSTRAINT `FK_ROL_DEPARTAMENTO` FOREIGN KEY (`Id_departamento`) REFERENCES `departamento` (`Id_departamento`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `turno`
--
ALTER TABLE `turno`
  ADD CONSTRAINT `FK_TURNO_HORARIO` FOREIGN KEY (`Id_horario`) REFERENCES `horario` (`id_horario`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_USUARIO_ROL` FOREIGN KEY (`Rol`) REFERENCES `rol` (`Id_rol`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
