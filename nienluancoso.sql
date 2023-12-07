-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 07, 2023 at 01:58 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `nienluancoso`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_accounts`
--

CREATE TABLE `tbl_accounts` (
  `id_acc` int(11) NOT NULL,
  `fullname` varchar(40) NOT NULL,
  `username` varchar(50) NOT NULL,
  `gender` varchar(3) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `email` varchar(255) NOT NULL,
  `passwd` varchar(255) NOT NULL,
  `security_question` varchar(255) NOT NULL,
  `security_answer` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_accounts`
--

INSERT INTO `tbl_accounts` (`id_acc`, `fullname`, `username`, `gender`, `phone`, `email`, `passwd`, `security_question`, `security_answer`) VALUES
(2, 'Dương Hồng Đoan', 'QA', 'Nữ', '0839727654', 'dhdoan@gmail.com', '1310c0ca6c08172d62078946a932698b', 'Tên thành phố hoặc thị trấn mà bạn sinh ra?', 'Bạc Liêu'),
(4, 'Nguyen My Yen', 'Yen', 'Nữ', '0855522147', 'yen@gmail.com', '1310c0ca6c08172d62078946a932698b', 'Tên thành phố hoặc thị trấn mà bạn sinh ra?', 'Bac Lieu'),
(5, 'Dương Hồng Đoan', 'Hồng Đoan', 'Nữ', '0833322569', 'doan@gmail.com', '87eeea9bef6c3f6b82eb3206786b8ff9', 'Tên thành phố hoặc thị trấn mà bạn sinh ra?', 'Bạc Liêu');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_bill`
--

CREATE TABLE `tbl_bill` (
  `id_bill` int(11) NOT NULL,
  `date_created` date NOT NULL,
  `bill_boss` varchar(255) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `paid` varchar(10) NOT NULL,
  `electric_bill` decimal(10,0) NOT NULL,
  `water_bill` decimal(10,0) NOT NULL,
  `other_bill` decimal(10,0) NOT NULL,
  `sum_bill` decimal(10,0) NOT NULL,
  `id_render` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_bill`
--

INSERT INTO `tbl_bill` (`id_bill`, `date_created`, `bill_boss`, `note`, `paid`, `electric_bill`, `water_bill`, `other_bill`, `sum_bill`, `id_render`) VALUES
(5, '2023-11-07', 'ggg', '', 'Đã đóng', '385000', '200000', '40000', '1425000', 51),
(6, '2023-11-15', 'gdg', 'zđ', 'Chưa đóng', '35000', '100000', '40000', '275000', 53),
(11, '2023-10-18', 'adsa', 'ád', 'Đã đóng', '200000', '100000', '30000', '1330000', 0),
(12, '2023-12-05', 'doan', '', 'Chưa đóng', '38500', '50000', '40000', '928500', 54),
(13, '2023-12-06', 'Dương', '', 'Đã đóng', '35000', '50000', '40000', '2125000', 55);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_inforender`
--

CREATE TABLE `tbl_inforender` (
  `id_render` int(11) NOT NULL,
  `fullname` varchar(50) NOT NULL,
  `gender` varchar(3) NOT NULL,
  `CCCD` varchar(12) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `homeTown` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_inforender`
--

INSERT INTO `tbl_inforender` (`id_render`, `fullname`, `gender`, `CCCD`, `phone`, `homeTown`) VALUES
(3, 'Duong Doan', 'Nữ', '096255006325', '0835263485', 'Ca Ma'),
(6, 'Nguyen Minh Hoang', 'Nam', '092200002541', '0832411493', 'Bac Lieu'),
(7, 'Duong Hoang Oanh', 'Nữ', '092301554218', '0839625411', 'Ca Mau'),
(10, 'Hoang Van Liem', 'Nam', '094298212121', '0832414225', 'Tra Vinh'),
(11, 'Duong Hong Doan', 'Nữ', '095300002525', '0835225996', 'Bac Lieu'),
(12, 'Nguyen Thu Tam', 'Nữ', '093302552626', '0839696325', 'Ha Noi'),
(14, 'Tran Nguyet An', 'Nữ', '087320552255', '0836525223', 'Tien Giang'),
(17, 'Luu Bao Ngoc', 'Nữ', '096309235544', '0839727654', 'Quang Nam'),
(19, 'Nhat Thuy', 'Nữ', '087306214747', '0835252478', 'Bac Lieu'),
(21, 'Minh Ngoc', 'Nữ', '087310235625', '0832541744', 'Vung Tau'),
(23, 'Donna', 'Nữ', '093302008255', '0835221142', 'Bac Lieu'),
(24, 'Ngoc Bich', 'Nữ', '087388002588', '0839636658', 'Ninh Thuan'),
(25, 'Hoang Hau', 'Nữ', '093300002525', '0835225145', 'Ben Tre'),
(26, 'My Huyen', 'Nữ', '093302008254', '0832256963', 'Kien Giang'),
(27, 'Thanh Hoang', 'Nam', '093202005233', '0839721125', 'Hau Giang'),
(28, 'Toan Ven', 'Nam', '087200002211', '0832214125', 'Dong Nai'),
(29, 'Toan Tam', 'Nam', '086200002212', '0832214125', 'Dong Nai'),
(31, 'Hong Tram', 'Nữ', '095304002525', '0936525253', 'Long An'),
(34, 'Tinh Anh', 'Nữ', '096308232526', '0853636214', 'Tra Vinh'),
(36, 'Bao Chau', 'Nam', '095290252423', '0839725553', 'Vinh Long'),
(37, 'Vo Hoang Oanh', 'Nữ', '012395225566', '0935252658', 'Vinh Long'),
(38, 'Minh Hoang', 'Nam', '087202002566', '0832252339', 'Tien Giang'),
(39, 'Bach Tuyet', 'Nữ', '093399225566', '0832252144', 'Hau Giang'),
(42, 'Duong Hoang Yen', 'Nữ', '096320002522', '0839665256', 'Tra Vinh'),
(43, 'Thach Thao', 'Nữ', '091388002526', '0832256339', 'Tien Giang'),
(44, 'Hong Tram', 'Nữ', '093304002524', '0836565236', 'Bac Lieu'),
(45, 'Hoang Nam', 'Nam', '092200007788', '0839696417', 'Kien Giang'),
(49, 'Anh Toan', 'Nam', '094299002211', '0832299884', 'Dong Thap'),
(51, 'My Yen', 'Nữ', '095308002211', '0832255447', 'Ca Mau'),
(52, 'Hoang Thi', 'Nam', '094200008855', '0833355669', 'Dong Thap'),
(53, 'Chu An Van', 'Nam', '091284002266', '0833322569', 'Dong Thap'),
(54, 'Dương Hoàng Cầm', 'Nam', '095322663311', '0855544126', 'Vĩnh Long'),
(55, 'Dương Hồng Đoan', 'Nữ', '095322554411', '0833322569', 'Bạc Liêu');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_rendroom`
--

CREATE TABLE `tbl_rendroom` (
  `id_rendRoom` int(11) NOT NULL,
  `check_in_date` date NOT NULL,
  `deposit` decimal(10,0) NOT NULL DEFAULT 0,
  `init_electric_indicator` int(11) NOT NULL,
  `init_water_indicator` int(11) NOT NULL,
  `id_room` int(11) NOT NULL,
  `id_render` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_rendroom`
--

INSERT INTO `tbl_rendroom` (`id_rendRoom`, `check_in_date`, `deposit`, `init_electric_indicator`, `init_water_indicator`, `id_room`, `id_render`) VALUES
(7, '2023-09-29', '0', 0, 0, 21, 3),
(8, '2023-10-12', '500000', 0, 0, 26, 4),
(9, '2023-10-17', '800000', 0, 0, 26, 5),
(10, '2023-10-26', '0', 0, 0, 27, 6),
(11, '2023-10-24', '0', 0, 0, 27, 7),
(12, '2023-10-20', '1000000', 0, 0, 26, 8),
(13, '2023-10-12', '1000000', 0, 0, 26, 9),
(14, '2023-10-04', '1000000', 0, 0, 21, 10),
(15, '2023-10-19', '500000', 0, 0, 21, 11),
(16, '2023-10-04', '0', 0, 0, 27, 12),
(17, '2023-10-25', '800000', 0, 0, 26, 13),
(18, '2023-10-10', '1000000', 0, 0, 21, 14),
(19, '2023-09-26', '800000', 0, 0, 26, 15),
(20, '2023-09-26', '800000', 0, 0, 26, 16),
(21, '2023-09-26', '0', 0, 0, 27, 17),
(22, '2023-10-11', '800000', 0, 0, 26, 18),
(23, '2023-10-04', '0', 0, 0, 27, 19),
(24, '2023-10-11', '800000', 0, 0, 26, 20),
(25, '2023-10-03', '1000000', 0, 0, 21, 21),
(26, '2023-10-19', '0', 0, 0, 27, 22),
(27, '2023-10-19', '0', 0, 0, 27, 23),
(28, '2023-10-04', '1000000', 0, 0, 21, 24),
(29, '2023-10-03', '0', 0, 0, 27, 25),
(30, '2023-10-03', '0', 0, 0, 27, 26),
(31, '2023-10-18', '0', 0, 0, 21, 27),
(32, '2023-10-28', '0', 0, 0, 27, 28),
(33, '2023-10-28', '0', 0, 0, 27, 29),
(34, '2023-10-28', '0', 0, 0, 26, 30),
(35, '2023-10-12', '0', 0, 0, 24, 31),
(36, '2023-10-12', '0', 0, 0, 26, 32),
(37, '2023-10-18', '800000', 0, 0, 26, 33),
(38, '2023-10-25', '0', 0, 0, 27, 34),
(39, '2023-10-25', '0', 0, 0, 24, 35),
(40, '2023-10-12', '0', 0, 0, 24, 36),
(41, '2023-10-18', '0', 0, 0, 21, 37),
(42, '2023-10-11', '0', 0, 0, 21, 38),
(43, '2023-10-03', '0', 0, 0, 24, 39),
(44, '2023-10-19', '0', 0, 0, 26, 22),
(45, '2023-10-18', '0', 0, 0, 27, 41),
(46, '2023-10-30', '0', 0, 0, 27, 42),
(47, '2023-10-31', '0', 0, 0, 29, 43),
(48, '2023-10-31', '0', 0, 0, 15, 31),
(49, '2023-10-31', '0', 0, 0, 26, 45),
(50, '2023-10-31', '0', 0, 0, 29, 46),
(51, '2023-10-30', '0', 0, 0, 29, 47),
(52, '2023-10-31', '0', 0, 0, 2, 48),
(53, '2023-11-01', '0', 0, 0, 27, 49),
(54, '2023-10-31', '0', 2001, 100, 30, 50),
(55, '2023-11-02', '100000', 100, 10, 22, 51),
(56, '2023-10-31', '250000', 300, 100, 2, 52),
(57, '2023-11-15', '0', 100, 10, 31, 53),
(58, '2023-12-05', '0', 100, 30, 30, 54),
(59, '2023-12-06', '0', 120, 20, 35, 55);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_room`
--

CREATE TABLE `tbl_room` (
  `id_room` int(11) NOT NULL,
  `roomName` varchar(30) NOT NULL,
  `roomStatus` varchar(20) NOT NULL,
  `id_roomType` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_room`
--

INSERT INTO `tbl_room` (`id_room`, `roomName`, `roomStatus`, `id_roomType`) VALUES
(2, 'P01', 'Đã thuê', 63),
(7, 'P06', 'Phòng trống', 62),
(22, 'P07', 'Đã thuê', 64),
(26, 'P10', 'Đã thuê', 65),
(30, 'P13', 'Đã thuê', 64),
(31, 'P14', 'Đã thuê', 62),
(35, 'P03', 'Đã thuê', 90);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_roomtype`
--

CREATE TABLE `tbl_roomtype` (
  `id_roomType` int(11) NOT NULL,
  `roomTypeName` varchar(50) NOT NULL,
  `roomPrice` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_roomtype`
--

INSERT INTO `tbl_roomtype` (`id_roomType`, `roomTypeName`, `roomPrice`) VALUES
(62, 'Binh Dan', '100000'),
(63, 'VIP', '2000000'),
(64, '1 người ở', '800000'),
(65, '2 người ở', '1000000'),
(90, 'Gia Đình', '2000000');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_service`
--

CREATE TABLE `tbl_service` (
  `id_service` int(11) NOT NULL,
  `new_indicator` int(11) NOT NULL,
  `old_indicator` int(11) NOT NULL,
  `id_serviceType` int(11) NOT NULL,
  `id_room` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_service`
--

INSERT INTO `tbl_service` (`id_service`, `new_indicator`, `old_indicator`, `id_serviceType`, `id_room`) VALUES
(1, 110, 100, 1, 22),
(2, 20, 10, 2, 22),
(3, 366, 366, 1, 2),
(4, 144, 144, 2, 2),
(5, 110, 100, 1, 31),
(6, 20, 10, 2, 31),
(7, 111, 111, 1, 30),
(8, 35, 35, 2, 30),
(9, 130, 120, 1, 35),
(10, 25, 20, 2, 35);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_servicetype`
--

CREATE TABLE `tbl_servicetype` (
  `id_serviceType` int(11) NOT NULL,
  `serviceName` varchar(255) NOT NULL,
  `servicePrice` decimal(10,0) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `tbl_servicetype`
--

INSERT INTO `tbl_servicetype` (`id_serviceType`, `serviceName`, `servicePrice`) VALUES
(1, 'Dien', '3500'),
(2, 'Nước', '10000'),
(4, 'Tiền rác', '10000'),
(7, 'Tiền wifi', '30000');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_accounts`
--
ALTER TABLE `tbl_accounts`
  ADD PRIMARY KEY (`id_acc`);

--
-- Indexes for table `tbl_bill`
--
ALTER TABLE `tbl_bill`
  ADD PRIMARY KEY (`id_bill`);

--
-- Indexes for table `tbl_inforender`
--
ALTER TABLE `tbl_inforender`
  ADD PRIMARY KEY (`id_render`);

--
-- Indexes for table `tbl_rendroom`
--
ALTER TABLE `tbl_rendroom`
  ADD PRIMARY KEY (`id_rendRoom`);

--
-- Indexes for table `tbl_room`
--
ALTER TABLE `tbl_room`
  ADD PRIMARY KEY (`id_room`);

--
-- Indexes for table `tbl_roomtype`
--
ALTER TABLE `tbl_roomtype`
  ADD PRIMARY KEY (`id_roomType`);

--
-- Indexes for table `tbl_service`
--
ALTER TABLE `tbl_service`
  ADD PRIMARY KEY (`id_service`);

--
-- Indexes for table `tbl_servicetype`
--
ALTER TABLE `tbl_servicetype`
  ADD PRIMARY KEY (`id_serviceType`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_accounts`
--
ALTER TABLE `tbl_accounts`
  MODIFY `id_acc` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tbl_bill`
--
ALTER TABLE `tbl_bill`
  MODIFY `id_bill` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `tbl_inforender`
--
ALTER TABLE `tbl_inforender`
  MODIFY `id_render` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;

--
-- AUTO_INCREMENT for table `tbl_rendroom`
--
ALTER TABLE `tbl_rendroom`
  MODIFY `id_rendRoom` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;

--
-- AUTO_INCREMENT for table `tbl_room`
--
ALTER TABLE `tbl_room`
  MODIFY `id_room` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `tbl_roomtype`
--
ALTER TABLE `tbl_roomtype`
  MODIFY `id_roomType` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `tbl_service`
--
ALTER TABLE `tbl_service`
  MODIFY `id_service` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `tbl_servicetype`
--
ALTER TABLE `tbl_servicetype`
  MODIFY `id_serviceType` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
