package com.all4land.generator.system.constant;

import java.awt.Color;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface SystemConstMessage {
	//
	String vsiStartIndex = "$ABVSI";
	String resourceStartIndex = "$resourceStart";
	String resourceResponseIndex = "$resourceResponse";
	String tsrStartIndex = "$VATSR";
	String StartIndex_for_450 = "UdPbC";
	
	
	String CRLF = "\r\n";
	String CR = "\r";
	Color defaultCellColor = new Color(40, 40, 40);
//	Color defaultCellColor = new Color(255, 255, 255);
	Color empty_Color = new Color(40, 40, 40);
	
	Color BBSC_Color = new Color(255, 255, 153);
	Color RAC_Color = new Color(204, 255, 204);
	Color ASC_Color = new Color(255, 204, 255);
	Color DSCH_Color = new Color(255, 204, 153);
	
	Color AIS_Color = new Color(98, 92, 255);
	Color ASM_Color = new Color(255, 0, 0);
	Color VDE_Color = new Color(5, 201, 207);
	
	Color ACK_Color = new Color(155, 1, 156);
	
	Color RESOURCE_REQUEST_Color = new Color(255, 0, 0);
	Color RESOURCE_RESPONSE_Color = new Color(240, 134, 80);
	Color RESOURCE_Color = new Color(0, 255, 0);
	
	DateTimeFormatter formatterForStartIndex = DateTimeFormatter.ofPattern("ss.SSSS");
	DateTimeFormatter formatterForTooltip = DateTimeFormatter.ofPattern("HH:mm:ss.SSSS");
	DateTimeFormatter mmssSSSS = DateTimeFormatter.ofPattern("mm:ss.SSSS");
	DateTimeFormatter formatterForLogDisplay = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSSSSS");
	DateTimeFormatter formatterForVSI = DateTimeFormatter.ofPattern("HHmmss.SS");
	DateTimeFormatter formatterForESI = DateTimeFormatter.ofPattern("HHmmss.SS");
	DateTimeFormatter formatterSlotNumberHeader = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm");
	
	int PREFIX_CONTINUES_COUNT_0 = 0;
	int PREFIX_CONTINUES_COUNT_1 = 1000;
	int PREFIX_CONTINUES_COUNT_2 = 2000;
	int PREFIX_CONTINUES_COUNT_3 = 3000;
	int PREFIX_CONTINUES_COUNT_4 = 4000;
	int PREFIX_CONTINUES_COUNT_5 = 5000;
	int PREFIX_CONTINUES_COUNT_6 = 6000;
	int PREFIX_CONTINUES_COUNT_7 = 7000;
	int PREFIX_CONTINUES_COUNT_8 = 8000;
	int PREFIX_CONTINUES_COUNT_9 = 9000;
	int PREFIX_CONTINUES_COUNT_10 = 10000;
	
	Map<Integer, double[]> positions_180_0 = new HashMap<Integer, double[]>() {
		{
			put(0, new double[] { 35.07487, 129.0942 });
		}
	};

	Map<Integer, double[]> positions_180_1 = new HashMap<Integer, double[]>() {
		{
			put(0, new double[] { 35.08420, 129.0807 });
		}
	};

	Map<Integer, double[]> positions_180_2 = new HashMap<Integer, double[]>() {
		{
			put(0, new double[] { 35.10221, 129.0888 });
		}
	};

	Map<Integer, double[]> positions_180_3 = new HashMap<Integer, double[]>() {
		{
			put(0, new double[] { 35.09519, 129.1041 });
		}
	};

	Map<Integer, double[]> positions_180_4 = new HashMap<Integer, double[]>() {
		{
			put(0, new double[] { 35.09902, 129.1081 });
		}
	};
	
	// 2초 10개 =======================================================================
	Map<Integer, double[]> positions_2_0 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.10464, 129.2169 });
			put(1, new double[] { 35.10356, 129.1978 });
			put(2, new double[] { 35.10132, 129.1720 });
			put(3, new double[] { 35.09960, 129.1452 });
			put(4, new double[] { 35.09998, 129.1276 });
		}
	};
	
	Map<Integer, double[]> positions_2_1 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.09417, 129.2212 });
			put(1, new double[] { 35.09308, 129.2010 });
			put(2, new double[] { 35.09263, 129.1753 });
			put(3, new double[] { 35.09372, 129.1543 });
			put(4, new double[] { 35.09678, 129.1272 });
		}
	};
	
	Map<Integer, double[]> positions_2_2 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.05263, 129.2117 });
			put(1, new double[] { 35.06560, 129.1981 });
			put(2, new double[] { 35.07308, 129.1761 });
			put(3, new double[] { 35.07781, 129.1497 });
			put(4, new double[] { 35.07928, 129.1162 });
		}
	};
	
	Map<Integer, double[]> positions_2_3 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.01957, 129.1236 });
			put(1, new double[] { 35.03690, 129.1206 });
			put(2, new double[] { 35.05064, 129.1062 });
			put(3, new double[] { 35.06375, 129.1004 });
			put(4, new double[] { 35.07672, 129.1028 });
		}
	};
	
	Map<Integer, double[]> positions_2_4 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.01574, 129.1812 });
			put(1, new double[] { 35.03019, 129.1614 });
			put(2, new double[] { 35.04451, 129.1426 });
			put(3, new double[] { 35.06106, 129.1240 });
			put(4, new double[] { 35.07685, 129.1047 });
		}
	};
	
	Map<Integer, double[]> positions_2_5 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.09340, 129.1179 });
			put(1, new double[] { 35.08829, 129.1261 });
			put(2, new double[] { 35.09455, 129.1372 });
			put(3, new double[] { 35.11499, 129.1434 });
			put(4, new double[] { 35.13766, 129.1639 });
		}
	};
	
	Map<Integer, double[]> positions_2_6 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.08822, 129.1165 });
			put(1, new double[] { 35.08573, 129.1381 });
			put(2, new double[] { 35.09020, 129.1650 });
			put(3, new double[] { 35.09199, 129.1999 });
			put(4, new double[] { 35.08758, 129.2448 });
		}
	};
	
	Map<Integer, double[]> positions_2_7 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.06420, 129.0481 });
			put(1, new double[] { 35.04938, 129.0723 });
			put(2, new double[] { 35.03615, 129.0857 });
			put(3, new double[] { 35.01699, 129.0824 });
			put(4, new double[] { 35.00390, 129.1147 });
		}
	};
	
	Map<Integer, double[]> positions_2_8 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.06982, 129.1082 });
			put(1, new double[] { 35.04717, 129.1280 });
			put(2, new double[] { 35.02398, 129.1277 });
			put(3, new double[] { 35.01394, 129.1668 });
			put(4, new double[] { 34.97895, 129.1898 });
		}
	};
	
	Map<Integer, double[]> positions_2_9 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.08426, 129.1068 });
			put(1, new double[] { 35.07325, 129.1386 });
			put(2, new double[] { 35.06111, 129.1720 });
			put(3, new double[] { 35.04157, 129.2169 });
			put(4, new double[] { 35.00483, 129.2484 });
		}
	};
	//==============================================================================================
	
	Map<Integer, double[]> positions_10_0 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.09794, 129.0671 });
			put(1, new double[] { 35.09247, 129.0774 });
			put(2, new double[] { 35.08547, 129.0867 });
			put(3, new double[] { 35.08126, 129.0992 });
			put(4, new double[] { 35.08019, 129.1055 });
		}
	};
	
	Map<Integer, double[]> positions_10_1 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.10318, 129.0889 });
			put(1, new double[] { 35.09574, 129.0926 });
			put(2, new double[] { 35.09046, 129.0968 });
			put(3, new double[] { 35.08430, 129.1049 });
			put(4, new double[] { 35.08293, 129.1127 });
		}
	};
	
	Map<Integer, double[]> positions_10_2 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.08366, 129.0931 });
			put(1, new double[] { 35.08063, 129.1055 });
			put(2, new double[] { 35.08518, 129.1146 });
			put(3, new double[] { 35.09178, 129.1183 });
			put(4, new double[] { 35.09589, 129.1249 });
		}
	};
	
	Map<Integer, double[]> positions_10_3 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.09031, 129.1085 });
			put(1, new double[] { 35.09359, 129.1149 });
			put(2, new double[] { 35.09599, 129.1240 });
			put(3, new double[] { 35.10200, 129.1311 });
			put(4, new double[] { 35.11472, 129.1310 });
		}
	};
	
	Map<Integer, double[]> positions_10_4 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.07671, 129.0963 });
			put(1, new double[] { 35.07260, 129.0936 });
			put(2, new double[] { 35.06854, 129.0880 });
			put(3, new double[] { 35.06272, 129.0940 });
			put(4, new double[] { 35.05988, 129.1035 });
		}
	};
	
	Map<Integer, double[]> positions_10_5 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.06326, 129.1148 });
			put(1, new double[] { 35.06991, 129.1033 });
			put(2, new double[] { 35.07862, 129.0968 });
			put(3, new double[] { 35.08376, 129.0962 });
			put(4, new double[] { 35.08449, 129.0876 });
		}
	};
	
	Map<Integer, double[]> positions_10_6 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.06316, 129.1297 });
			put(1, new double[] { 35.07153, 129.1189 });
			put(2, new double[] { 35.08009, 129.1082 });
			put(3, new double[] { 35.08405, 129.0996 });
			put(4, new double[] { 35.08792, 129.0908 });
		}
	};
	
	Map<Integer, double[]> positions_10_7 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.08307, 129.1112 });
			put(1, new double[] { 35.08430, 129.1043 });
			put(2, new double[] { 35.09012, 129.0959 });
			put(3, new double[] { 35.09677, 129.0846 });
			put(4, new double[] { 35.10357, 129.0733 });
		}
	};
	
	Map<Integer, double[]> positions_10_8 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.09002, 129.0995 });
			put(1, new double[] { 35.09643, 129.0878 });
			put(2, new double[] { 35.10195, 129.0758 });
			put(3, new double[] { 35.10826, 129.0624 });
			put(4, new double[] { 35.11413, 129.0548 });
		}
	};
	
	Map<Integer, double[]> positions_10_9 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.08532, 129.0986 });
			put(1, new double[] { 35.08904, 129.0876 });
			put(2, new double[] { 35.09496, 129.0771 });
			put(3, new double[] { 35.10215, 129.0650 });
			put(4, new double[] { 35.11002, 129.0629 });
		}
	};
	
	//===========================================================================================
	
	Map<Integer, double[]> positions_6_0 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] {35.12645, 129.1641  });
			put(1, new double[] { 35.11853, 129.1606 });
			put(2, new double[] { 35.10856, 129.1519 });
			put(3, new double[] { 35.09789, 129.1425 });
			put(4, new double[] { 35.08679, 129.1299 });
		}
	};
	
	Map<Integer, double[]> positions_6_1 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.07256, 129.1835 });
			put(1, new double[] { 35.07676, 129.1708 });
			put(2, new double[] { 35.08038, 129.1558 });
			put(3, new double[] { 35.08302, 129.1382 });
			put(4, new double[] { 35.08850, 129.1209 });
		}
	};
	
	Map<Integer, double[]> positions_6_2 = new HashMap<Integer, double[]>() { // 들어오는
		{
			put(0, new double[] { 35.06443, 129.1562 });
			put(1, new double[] { 35.06585, 129.1420 });
			put(2, new double[] { 35.07363, 129.1204 });
			put(3, new double[] { 35.08293, 129.1041 });
			put(4, new double[] { 35.08870, 129.0932 });
		}
	};
	
	Map<Integer, double[]> positions_6_3 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.08620, 129.1160 });
			put(1, new double[] { 35.08102, 129.1315 });
			put(2, new double[] { 35.07039, 129.1482 });
			put(3, new double[] { 35.05696, 129.1618 });
			put(4, new double[] { 35.04162, 129.1800 });
		}
	};
	
	Map<Integer, double[]> positions_6_4 = new HashMap<Integer, double[]>() { // 나가는
		{
			put(0, new double[] { 35.08254, 129.1030 });
			put(1, new double[] { 35.07117, 129.1182 });
			put(2, new double[] { 35.05392, 129.1355 });
			put(3, new double[] { 35.03623, 129.1543 });
			put(4, new double[] { 35.01870, 129.1717 });
		}
	};
	
	
	Set<Integer> TSQ_TEST_SLOT_NUMBER_ALL = new HashSet<>(Arrays.asList(
			18, 30, 42, 54, 66, 78, 108, 120, 132, 144, 156, 168
			, 198, 210, 222, 234, 246, 258, 288, 300, 312, 324, 336, 348
			, 378, 390, 402, 414, 426, 438, 468, 480, 492, 504, 516, 528
			, 558, 570, 582, 594, 606, 618, 648, 660, 672, 684, 696, 708
			, 738, 750, 762, 774, 786, 798, 828, 840, 852, 864, 876, 888
			, 918, 930, 942, 954, 966, 978, 1008, 1020, 1032, 1044, 1056, 1068
			, 1098, 1110, 1122, 1134, 1146, 1158, 1188, 1200, 1212, 1224, 1236, 1248
			, 1278, 1290, 1302, 1314, 1326, 1338, 1368, 1380, 1392, 1404, 1416, 1428
			, 1458, 1470, 1482, 1494, 1506, 1518, 1548, 1560, 1572, 1584, 1596, 1608
			, 1638, 1650, 1662, 1674, 1686, 1698, 1728, 1740, 1752, 1764, 1776, 1788
			, 1818, 1830, 1842, 1854, 1866, 1878, 1908, 1920, 1932, 1944, 1956, 1968
			, 1998, 2010, 2022, 2034, 2046, 2058, 2088, 2100, 2112, 2124, 2136, 2148
			, 2178, 2190, 2202, 2214, 2226, 2238));
	
	Set<Integer> TSQ_TEST_SLOT_NUMBER_ALL2 = new HashSet<>(Arrays.asList(
			18//, 108
			//, 198, 288
			, 378//, 468
			//, 558, 648
			, 738//, 828
			//, 918, 1008
			, 1098//, 1188
			//, 1278, 1368
			, 1458//, 1548
			//, 1638, 1728
			, 1818//, 1908
			//, 1998, 2088
			, 2178));
	
	Set<Integer> TSQ_TEST_SLOT_NUMBER_1 = new HashSet<>(Arrays.asList(
			18//, 108
			//, 198, 288
			, 378//, 468
			//, 558, 648
			, 738//, 828
			//, 918, 1008
			, 1098//, 1188
			//, 1278, 1368
			, 1458//, 1548
			//, 1638, 1728
			, 1818//, 1908
			//, 1998, 2088
			, 2178));
}
