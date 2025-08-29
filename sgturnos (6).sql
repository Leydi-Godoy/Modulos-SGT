-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 29-08-2025 a las 12:33:40
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
  `Id_turno` bigint(20) NOT NULL,
  `Id_colaborador` bigint(20) NOT NULL,
  `fecha` date NOT NULL,
  `observaciones` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asignacion_turno`
--

INSERT INTO `asignacion_turno` (`Id_turno`, `Id_colaborador`, `fecha`, `observaciones`) VALUES
(8, 4, '2025-09-01', 'Cobertura mínima DIA'),
(8, 5, '2025-09-01', 'Cobertura mínima DIA'),
(8, 6, '2025-09-01', 'Cobertura mínima DIA'),
(8, 7, '2025-09-01', 'Cobertura mínima DIA'),
(8, 8, '2025-09-01', 'Cobertura mínima DIA'),
(8, 9, '2025-09-01', 'Cobertura mínima DIA'),
(8, 10, '2025-09-01', 'Cobertura mínima DIA'),
(8, 11, '2025-09-01', 'Cobertura mínima DIA'),
(8, 15, '2025-09-01', 'Cobertura mínima DIA'),
(8, 16, '2025-09-01', 'Cobertura mínima DIA'),
(8, 17, '2025-09-01', 'Cobertura mínima DIA'),
(8, 18, '2025-09-01', 'Cobertura mínima DIA'),
(8, 19, '2025-09-01', 'Cobertura mínima DIA'),
(8, 23, '2025-09-01', 'Cobertura mínima DIA'),
(8, 24, '2025-09-01', 'Cobertura mínima DIA'),
(9, 12, '2025-09-01', 'Cobertura mínima NOCHE'),
(9, 13, '2025-09-01', 'Cobertura mínima NOCHE'),
(9, 14, '2025-09-01', 'Cobertura mínima NOCHE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `capacitacion`
--

CREATE TABLE `capacitacion` (
  `Id_capacitacion` int(11) NOT NULL,
  `Id_tema` varchar(30) NOT NULL,
  `Fecha` date NOT NULL,
  `Id_estado_cap` varchar(30) NOT NULL,
  `Id_colaborador` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `colaborador`
--

CREATE TABLE `colaborador` (
  `Id_colaborador` bigint(20) NOT NULL,
  `id_rol` varchar(255) DEFAULT NULL,
  `Id_usuario` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `colaborador`
--

INSERT INTO `colaborador` (`Id_colaborador`, `id_rol`, `Id_usuario`) VALUES
(1, 'adm05', 80101476),
(2, 'adm05', 1104774847),
(3, 'adm05', 1110110120),
(4, 'aux01', 1101101101),
(5, 'aux01', 1104104101),
(6, 'aux01', 1110101110),
(7, 'aux01', 1110110111),
(8, 'aux01', 1110110112),
(9, 'aux01', 1110110114),
(10, 'aux01', 1110110115),
(11, 'aux01', 1110110116),
(12, 'aux01', 1110110117),
(13, 'aux01', 1110110118),
(14, 'aux01', 1110110142),
(15, 'enf02', 1107107107),
(16, 'enf02', 1108108104),
(17, 'enf02', 1109109101),
(18, 'med03', 1102102101),
(19, 'med03', 1105105104),
(20, 'med03', 1110110122),
(21, 'med03', 1110110146),
(22, 'med03', 1110110241),
(23, 'ter04', 110681104),
(24, 'ter04', 1103103101),
(25, 'ter04', 1110110113),
(26, 'ter04', 1110110121),
(27, 'ter04', 1110110149),
(28, 'ter04', 1110110157),
(32, 'enf02', 1110110148),
(33, 'enf02', 1110110150),
(34, 'enf02', 1110110151),
(35, 'aux01', 1110110152),
(36, 'aux01', 1110110153),
(37, 'aux01', 1110110154);

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
-- Estructura de tabla para la tabla `horario`
--

CREATE TABLE `horario` (
  `id_horario` varchar(255) NOT NULL,
  `hora_inicio` varchar(255) DEFAULT NULL,
  `hora_fin` varchar(255) DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `horario`
--

INSERT INTO `horario` (`id_horario`, `hora_inicio`, `hora_fin`, `tipo`) VALUES
('C01', '10:00:00', '13:00:00', 'COMITE'),
('h01', '07:00:00', '19:00:00', 'DIA'),
('h02', '19:00:00', '07:00:00', 'NOCHE'),
('h03', '07:00:00', '19:00:00', 'LIBRE');

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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `rol`
--

CREATE TABLE `rol` (
  `id_rol` varchar(255) NOT NULL,
  `rol` varchar(255) DEFAULT NULL,
  `id_departamento` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `rol`
--

INSERT INTO `rol` (`id_rol`, `rol`, `id_departamento`) VALUES
('adm05', 'ADMINISTRADOR', 'D003'),
('aux01', 'AUXILIAR', 'enfer01'),
('enf02', 'ENFERMERO', 'enfer01'),
('med03', 'MEDICO', 'medic02'),
('ter04', 'TERAPIA', 'eqreh03');

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
  `Id_turno` bigint(20) NOT NULL,
  `Fecha_ini` date NOT NULL,
  `Fecha_fin` date NOT NULL,
  `id_horario` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `turno`
--

INSERT INTO `turno` (`Id_turno`, `Fecha_ini`, `Fecha_fin`, `id_horario`) VALUES
(1, '2025-04-15', '2025-04-15', '0'),
(2, '2025-04-16', '2025-04-17', '0'),
(3, '2025-04-17', '2025-04-17', '0'),
(4, '2025-04-16', '2025-04-17', '0'),
(5, '2025-04-19', '2025-04-19', '0'),
(6, '2025-04-16', '2025-04-17', '0'),
(7, '2025-09-01', '2025-09-01', '0'),
(8, '2025-09-01', '2025-09-01', 'h01'),
(9, '2025-09-01', '2025-09-01', 'h02');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `Id_usuario` bigint(20) NOT NULL,
  `primer_nombre` varchar(255) DEFAULT NULL,
  `segundo_nombre` varchar(255) DEFAULT NULL,
  `primer_apellido` varchar(255) DEFAULT NULL,
  `segundo_apellido` varchar(255) DEFAULT NULL,
  `id_rol` varchar(255) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `contrasena` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`Id_usuario`, `primer_nombre`, `segundo_nombre`, `primer_apellido`, `segundo_apellido`, `id_rol`, `correo`, `contrasena`) VALUES
(80101476, 'Edisson', 'Andrés', 'Taborda', 'Reyes', 'adm05', 'edissontaborda@paliacare.com', '$2a$10$TNNqfKXw.NYc5GwJiJ5pB.k1Sdfam8Jqt95GTyLvd0DfR18HZvcV6'),
(110681104, 'Genny', 'Carolina', 'Murcia', 'Vargas', 'ter04', 'gennymurcia@paliacare.com', '$2a$10$T3O/m/xNp18oXsm4o85QyeX4o5zyhiWrFu7JbLiypLe6mF76h832O'),
(1101101101, 'Yuliy', 'Paola', 'Daza', 'Oviedo', 'aux01', 'yuliydaza@paliacare.com', '$2a$10$kQ/V.vkCHYq..xPW5/a2feZouwN.j5K/8kzgSkDNosVUBTooeQQQC'),
(1102102101, 'Melissa', 'Andrea', 'Solano', 'Patiño', 'med03', 'melissasolano@paliacare.com', '$2a$10$dr/bUSh2EYUeeNbYqNtQuOHD.YXwsJofP5e2Q7GEHONKSUcW8qsVq'),
(1103103101, 'Angelica', 'Milena', 'Prada', 'Cañón', 'ter04', 'angelicaprada@paliacare.com', '$2a$10$21A6rXqQFXZ9gFYX.Wqs9uFbV847yBlRDF0acrOh75Ayh7PhA7pEe'),
(1104104101, 'Jesús', 'Daniel', 'Beltrán', 'Rodríguez', 'aux01', 'jesusbeltran@paliacare.com', '$2a$10$NVlLIr1vOfQqebqW7aZmC.SHOGsUYhUw.yBWaWmVeaxPIyFqALeku'),
(1104774847, 'Leydi', 'Cecilia', 'Godoy', 'Ortiz', 'adm05', 'leydigodoy@paliacare.com', '$2a$10$D3Yu77JIOqBCG6uMz9fdOubRZwAbLSQR0oRSIyeA6t7Jipbqnmxky'),
(1105105104, 'Carlos', 'Andrés', 'Rodríguez', 'Ochoa', 'med03', 'carlosrodriguez@paliacare.com', '$2a$10$aGwJ5dRA.yJVEESep6UJQuJ2idQemauoDK5ZQaZirJZyQXqWYaY/2'),
(1107107107, 'Jenny', 'Andrea', 'Martinez', 'Heredia', 'enf02', 'jennymartinez@paliacare.com', '$2a$10$ItlJPq9yAWRaQVti1z55re/rJQT60oVVRHwL4dQTpXl4W8lt44wYO'),
(1108108104, 'María', 'Camila', 'barajas', 'López', 'enf02', 'mariabarajas@paliacare.com', '$2a$10$lc1T7Qt/tFr0d4wuB2pREOyP4.h7U/wzgaXIKs586Nkym9h6DL/S2'),
(1109109101, 'Armando', 'Stiven', 'Silva', 'Rodríguez', 'enf02', 'armandosilva@paliacare.com', '$2a$10$fM9JpEYEV5v4moKFowlgZu9w1xT26/S6CYhgbuaZ4mE9kP/CrQ0ia'),
(1110101110, 'Mónica', 'Patricia', 'Pinilla', 'Castro', 'aux01', 'monicapinilla@paliacare.com', '$2a$10$YAHt9viHu9Dowld6r0qHye6utBzgYd7z4RkVHIj506DYZNF2btjye'),
(1110110111, 'Camila', 'Andrea', 'Vergara', 'Caro', 'aux01', 'camilavergara@paliacare.com', '$2a$10$7P3iAyrE1d4gwuchyCbyZ.3d279vUqzN43KpqpNA3hfQyAKea86iq'),
(1110110112, 'Andrés', 'Felipe', 'Castro', 'Polo', 'aux01', 'andrescastro@paliacare.com', '$2a$10$yKym7PQ5V3g8Yo3xv8mc8uew5y1BugU3otGujoocLNGtqzMm1IsTi'),
(1110110113, 'Julia', 'Fernanda', 'Araujo', 'Henao', 'ter04', 'juliaaraujo@paliacare.com', '$2a$10$UZG/0uk/oOjv4Ch.lsJfbOr0ucygCFr9v8M74ezKO6DOsHLBDGyKm'),
(1110110114, 'Juana', 'Carolina', 'López', 'Montes', 'aux01', 'juanalopez@paliacare.com', '$2a$10$wwnvmlh6pg5EzKGXE6uEFub5MLousNCWjHAQ74b2eO1DVYhS9stP6'),
(1110110115, 'Daniela', 'Carolina', 'Carvajal', 'Rio', 'aux01', 'danielacarvajal@paliacare.com', '$2a$10$hJP/KUXFzjoukzcS114lP.6WcwZpHlnw.2pPMUa67RNTAXTAxUzuu'),
(1110110116, 'Verónica', 'Sofia', 'Cantor', 'Jiménez', 'aux01', 'veronicacantor@paliacare.com', '$2a$10$crhPEt/e3cIrKyH4dm4FGu4lXFP4pcJrwRtm3sAbsKJc6qr71x.Kq'),
(1110110117, 'Carla', 'Antonia', 'Muñoz', 'Álvarez', 'aux01', 'carlamunoz@paliacare.com', '$2a$10$BjW0NXOa6i5oR0R7s8/hNe7Pf97knUJhixwVnbEdQynhSHk.Cn3NS'),
(1110110118, 'Patricia', NULL, 'Paternina', NULL, 'aux01', 'patriciapaternina@paliacare.com', '$2a$10$u/aKSP6Mx3hroVnsi1Con.OxFjlFwcd./oXbAEl1p.KMWaFxou/jq'),
(1110110120, 'Santiago', 'Jose', 'Cardona', NULL, 'adm05', 'santiagocardona@paliacare.com', '$2a$10$pKbtPq6KyM0EKUbtwmqUROKmAsS3o.b..KDTBbUNkkaVnibu1YWWm'),
(1110110121, 'Marcela', NULL, 'Panqueva', 'Rios', 'ter04', 'marcelapanqueva@paliacare.com', '$2a$10$Ae2/xw/Xoe80POlyxgQ5SuYUdehCditbX.w50rlFSYzgIup3K23Bm'),
(1110110122, 'Erika', 'Amanda', 'Suarez', 'Parra', 'med03', 'amandasuarez@paliacare.com', '$2a$10$62f.794jr3Z2GD.LGK31zOV4TTJuJIV159O8IU1bBjwmwAfzUd4di'),
(1110110142, 'Yajaira', 'Paola', 'Rangel', 'Roa', 'aux01', 'yajairarangel@paliacare.com', '$2a$10$dX5yc0PZGKyLlg1pllcV2ujmEIuJOTErIGmH0c92XBqPJnj/OEVb6'),
(1110110146, 'Sandy', 'Lorena', 'Páez', 'Soto', 'med03', 'sandypaez@paliacare.com', '$2a$10$kAU6OdSpGnFTLhdntDHOt.hVxlX41JpTYp6rnQWovQf5hSqTEM5SO'),
(1110110148, 'Luis', 'Alberto', 'Martinez', 'Colorado', 'enf02', 'luismartinez@paliacare.com', '$2a$10$DH2nFj2mGzrSeL5DZlPuYe.bUSg3Rv1gxJ/vOlVSVQkCRFj4HCTKu'),
(1110110149, 'Sharon', 'Daniela', 'Vargas', 'Pava', 'enf02', 'sharonvargas@paliacare.com', '$2a$10$VjZBFXcEhUICgM7.r0oP4eO0xeitZmTB37irobiz/gT8FIzPSZBPm'),
(1110110150, 'David', 'Steban', 'Mendoza', 'Escobar', 'enf02', 'davidmendoza@paliacare.com', '$2a$10$bXmf4W/912ZdZOFI2eNcoOnIad9Pmapd1PBLN1dyeMqUjPNT7T1jK'),
(1110110151, 'Oscar', 'Anselmo', 'Valencia', 'Bravo', 'enf02', 'oscarvalencia@paliacare.com', '$2a$10$FsIrLWczzrbF6hjNcWWSc.5wzwhc1ZoW5TIvQ8LBXg1Q6cSTqw/5e'),
(1110110152, 'Alexander', '', 'Quintero', 'Duran', 'aux01', 'alexanderquintero@paliacare.com', '$2a$10$7gQAJT.3a9JCA8dSC2G3Bu2T0pIqSWhVlYvRvI1XqsSl/nc6Ze3CW'),
(1110110153, 'Angie', 'Daniela', 'Pastor', 'Leon', 'aux01', 'angiepastor@paliacare.com', '$2a$10$Axkk6ZWFjQybj1rPadFKie8Rpa1sk.XTB5FjXDsnKgZlWfYVCeY7a'),
(1110110154, 'Catalina', 'Maria', 'Leon', 'Bueno', 'aux01', 'catalinaleon@paliacare.com', '$2a$10$KyJ16eSp/tUad4sHG0gdluGxnn9lSdC89LdXy/54Cb01LGrMfNnma'),
(1110110157, 'Paula', 'Andrea', 'Bernal', 'Duitama', 'med03', 'paulabernal@paliacare.com', '$2a$10$1.vJ8mnw6ilQtvYgBcvvuurhTQf.bWNN0p0M4P7LAXuy.tXqDAjSa'),
(1110110241, 'Yohana', '', 'Hernandez', 'Marin', 'enf02', 'yohanahernandez@paliacare.com', '$2a$10$A.Am.YbTXKh6Jzkji/c4NeRw0H5.nfWVM.Xb.S9BVslKzLACrbBz6');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `asignacion_turno`
--
ALTER TABLE `asignacion_turno`
  ADD PRIMARY KEY (`Id_turno`,`Id_colaborador`,`fecha`),
  ADD KEY `FK_ASIGNACION_COLABORADOR` (`Id_colaborador`);

--
-- Indices de la tabla `capacitacion`
--
ALTER TABLE `capacitacion`
  ADD PRIMARY KEY (`Id_capacitacion`),
  ADD KEY `FK_CAPACITACION_COLABORADOR` (`Id_colaborador`),
  ADD KEY `FK_CAPACITACION_TEMA` (`Id_tema`),
  ADD KEY `FK_CAPACITACION_ESTADO` (`Id_estado_cap`);

--
-- Indices de la tabla `colaborador`
--
ALTER TABLE `colaborador`
  ADD PRIMARY KEY (`Id_colaborador`),
  ADD KEY `FK_COLABORADOR_ROL` (`id_rol`),
  ADD KEY `FK_COLABORADOR_USUARIO` (`Id_usuario`);

--
-- Indices de la tabla `departamento`
--
ALTER TABLE `departamento`
  ADD PRIMARY KEY (`Id_departamento`);

--
-- Indices de la tabla `estado_capacitacion`
--
ALTER TABLE `estado_capacitacion`
  ADD PRIMARY KEY (`Id_estado_cap`);

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
  ADD PRIMARY KEY (`id_rol`),
  ADD KEY `FK_ROL_DEPARTAMENTO` (`id_departamento`);

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
  ADD KEY `FK_TURNO_HORARIO` (`id_horario`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`Id_usuario`),
  ADD KEY `FK_USUARIO_ROL` (`id_rol`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `colaborador`
--
ALTER TABLE `colaborador`
  MODIFY `Id_colaborador` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=38;

--
-- AUTO_INCREMENT de la tabla `turno`
--
ALTER TABLE `turno`
  MODIFY `Id_turno` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asignacion_turno`
--
ALTER TABLE `asignacion_turno`
  ADD CONSTRAINT `FK_ASIGNACION_COLABORADOR` FOREIGN KEY (`Id_colaborador`) REFERENCES `colaborador` (`Id_colaborador`),
  ADD CONSTRAINT `FK_ASIGNACION_TURNO` FOREIGN KEY (`Id_turno`) REFERENCES `turno` (`Id_turno`);

--
-- Filtros para la tabla `capacitacion`
--
ALTER TABLE `capacitacion`
  ADD CONSTRAINT `FK_CAPACITACION_COLABORADOR` FOREIGN KEY (`Id_colaborador`) REFERENCES `colaborador` (`Id_colaborador`),
  ADD CONSTRAINT `FK_CAPACITACION_ESTADO` FOREIGN KEY (`Id_estado_cap`) REFERENCES `estado_capacitacion` (`Id_estado_cap`),
  ADD CONSTRAINT `FK_CAPACITACION_TEMA` FOREIGN KEY (`Id_tema`) REFERENCES `tema_capacitacion` (`Id_tema`);

--
-- Filtros para la tabla `colaborador`
--
ALTER TABLE `colaborador`
  ADD CONSTRAINT `FK_COLABORADOR_ROL` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`Id_rol`),
  ADD CONSTRAINT `FK_COLABORADOR_USUARIO` FOREIGN KEY (`Id_usuario`) REFERENCES `usuario` (`Id_usuario`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_USUARIO_ROL` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`Id_rol`),
  ADD CONSTRAINT `FKp5pt6m71kdbfpg38v1bupxnjf` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`Id_rol`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
