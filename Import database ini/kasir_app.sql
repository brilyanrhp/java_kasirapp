-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 15, 2020 at 08:56 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.2.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `kasir_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id_barang` int(21) NOT NULL,
  `nama_barang` varchar(21) NOT NULL,
  `harga_barang` varchar(21) NOT NULL,
  `stok` int(21) UNSIGNED NOT NULL DEFAULT 0,
  `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `karyawan`
--

CREATE TABLE `karyawan` (
  `id_karyawan` int(21) NOT NULL,
  `nama_karyawan` varchar(21) NOT NULL,
  `admin` tinyint(1) NOT NULL,
  `username` varchar(21) NOT NULL,
  `password` varchar(21) NOT NULL,
  `last_update` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tmp_karyawan`
--

CREATE TABLE `tmp_karyawan` (
  `id_karyawan` int(21) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `tmp_transaksi`
--

CREATE TABLE `tmp_transaksi` (
  `id_transaksi` int(21) NOT NULL,
  `id_barang` int(21) NOT NULL,
  `id_karyawan` int(21) NOT NULL,
  `tanggal_transaksi` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `qty` int(21) NOT NULL,
  `total` int(21) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(21) NOT NULL,
  `id_barang` int(21) NOT NULL,
  `id_karyawan` int(21) NOT NULL,
  `tanggal_transaksi` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `qty` int(21) NOT NULL,
  `total` int(21) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `transaksi`
--
DELIMITER $$
CREATE TRIGGER `Tmp` AFTER INSERT ON `transaksi` FOR EACH ROW INSERT INTO tmp_transaksi(id_transaksi, id_barang, id_karyawan, tanggal_transaksi, qty, total) VALUES (NEW.id_transaksi, NEW.id_barang, NEW.id_karyawan, NEW.tanggal_transaksi, NEW.qty, NEW.total)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `stok` AFTER INSERT ON `transaksi` FOR EACH ROW UPDATE barang set stok = stok - NEW.qty WHERE id_barang = NEW.id_barang
$$
DELIMITER ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id_barang`);

--
-- Indexes for table `karyawan`
--
ALTER TABLE `karyawan`
  ADD PRIMARY KEY (`id_karyawan`);

--
-- Indexes for table `tmp_karyawan`
--
ALTER TABLE `tmp_karyawan`
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Indexes for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `id_barang` (`id_barang`),
  ADD KEY `id_karyawan` (`id_karyawan`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tmp_karyawan`
--
ALTER TABLE `tmp_karyawan`
  ADD CONSTRAINT `tmp_karyawan_ibfk_1` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `transaksi_ibfk_2` FOREIGN KEY (`id_karyawan`) REFERENCES `karyawan` (`id_karyawan`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `transaksi_ibfk_3` FOREIGN KEY (`id_barang`) REFERENCES `barang` (`id_barang`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
