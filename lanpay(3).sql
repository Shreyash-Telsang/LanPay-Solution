-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 04, 2025 at 02:43 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lanpay`
--

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE `profile` (
  `Account` varchar(42) NOT NULL,
  `Private Key` varchar(66) NOT NULL,
  `Balance` int(255) UNSIGNED NOT NULL DEFAULT 0,
  `Name` varchar(255) NOT NULL,
  `Phone no.` varchar(10) NOT NULL,
  `Ration card no.` varchar(10) NOT NULL,
  `Mail id` varchar(255) DEFAULT NULL,
  `PIN` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`Account`, `Private Key`, `Balance`, `Name`, `Phone no.`, `Ration card no.`, `Mail id`, `PIN`) VALUES
('1234567890', '0', 0, 'Manthan', '1234567890', '1234567890', 'hello@gmail.com', 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`Account`),
  ADD UNIQUE KEY `Phone` (`Phone no.`),
  ADD UNIQUE KEY `Ration_card` (`Ration card no.`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
