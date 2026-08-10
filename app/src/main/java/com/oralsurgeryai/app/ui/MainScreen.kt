package com.oralsurgeryai.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.oralsurgeryai.app.data.CbctResponse
import com.oralsurgeryai.app.data.PrognosisResponse
import com.oralsurgeryai.app.ui.components.ClinicalLogo
import com.oralsurgeryai.app.ui.screens.*
import com.oralsurgeryai.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"
    
    var currentTitle by remember { mutableStateOf("Oral Surgery AI") }
    
    // Shared State
    var activeCbctResponse by remember { mutableStateOf<CbctResponse?>(null) }
    var activePrognosisResponse by remember { mutableStateOf<PrognosisResponse?>(null) }
    
    var showDisclaimer by remember { mutableStateOf(false) }

    if (showDisclaimer && com.oralsurgeryai.app.data.UserSession.userRole == "User") {
        var hasAcceptedPolicy by remember { mutableStateOf(false) }
        
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Clinical Use Agreement", fontWeight = FontWeight.Black) },
            text = {
                Column {
                    Text(
                        "This software is a Clinical Decision Support System (CDSS). By activating this session, you acknowledge:\n\n" +
                        "1. AI-generated results are for educational and support purposes only.\n" +
                        "2. Final surgical decisions must be validated by a licensed radiologist.\n" +
                        "3. Patient data processed must be anonymized per HIPAA standards.",
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = hasAcceptedPolicy, onCheckedChange = { hasAcceptedPolicy = it })
                        Text("I accept the Clinical Data Privacy Policy", style = MaterialTheme.typography.labelSmall)
                    }
                    Text("Version: 4.2.1-stable • System-ID: OSAI-PRD", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDisclaimer = false },
                    enabled = hasAcceptedPolicy,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Initialize Portal")
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            if (currentRoute != "login") {
                ModalDrawerSheet(
                    drawerContainerColor = Color.White,
                    drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                ) {
                    // Drawer content...
                    DrawerHeader()
                    Spacer(modifier = Modifier.height(24.dp))
                    DrawerItem("Dashboard", Icons.Default.Dashboard, currentRoute == "dashboard") { 
                        navController.navigate("dashboard"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("CBCT Viewer", Icons.Default.Biotech, currentRoute == "cbct_viewer") { 
                        navController.navigate("cbct_viewer"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("Decision Support", Icons.Default.Verified, currentRoute == "explainable_ai") { 
                        navController.navigate("explainable_ai"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("Nerve Analysis", Icons.AutoMirrored.Filled.AltRoute, currentRoute == "nerve_analysis") { 
                        navController.navigate("nerve_analysis"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("Tumor Analysis", Icons.Default.Warning, currentRoute == "tumor_analysis") { 
                        navController.navigate("tumor_analysis"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("Prognosis Tool", Icons.Default.Analytics, currentRoute == "prognosis") { 
                        navController.navigate("prognosis"); scope.launch { drawerState.close() } 
                    }
                    DrawerItem("Clinical Reports", Icons.Default.QueryStats, currentRoute == "reports") { 
                        navController.navigate("reports"); scope.launch { drawerState.close() } 
                    }

                    DrawerItem("System Settings", Icons.Default.Settings, currentRoute == "settings") {
                        navController.navigate("settings"); scope.launch { drawerState.close() } 
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    if (com.oralsurgeryai.app.data.UserSession.userRole == "Admin") {
                        DrawerItem("Admin Panel", Icons.Default.AdminPanelSettings, currentRoute == "admin_panel") { 
                            navController.navigate("admin_panel"); scope.launch { drawerState.close() } 
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        },
        gesturesEnabled = currentRoute != "login"
    ) {
        Scaffold(
            topBar = {
                if (currentRoute != "login") {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                ClinicalLogo(modifier = Modifier.size(28.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(currentTitle, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            }
                        },
                        navigationIcon = {
                            if (currentRoute == "dashboard") {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                                }
                            } else {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                    )
                }
            },
            bottomBar = {
                if (currentRoute != "login") {
                    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                        BottomNavItem(navController, "dashboard", "Home", Icons.Default.Dashboard)
                        BottomNavItem(navController, "cbct_viewer", "Scans", Icons.Default.Biotech)
                        BottomNavItem(navController, "explainable_ai", "Support", Icons.Default.Psychology)
                        BottomNavItem(navController, "reports", "Reports", Icons.Default.QueryStats)
                        BottomNavItem(navController, "settings", "Settings", Icons.Default.Settings)
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(if (currentRoute == "login") PaddingValues(0.dp) else innerPadding)
            ) {
                composable("login") {
                    LoginScreen(onLoginSuccess = {
                        showDisclaimer = true
                        navController.navigate("dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    })
                }
                composable("dashboard") {
                    currentTitle = "Clinical Dashboard"
                    DashboardScreen(
                        onViewCbct = { navController.navigate("cbct_viewer") },
                        onViewPrognosis = { navController.navigate("prognosis") },
                        onViewTraining = { navController.navigate("training") },
                        onViewAdmin = { navController.navigate("admin_panel") },
                        onViewNerve = { navController.navigate("nerve_analysis") },
                        onViewTumor = { navController.navigate("tumor_analysis") }
                    )
                }
                // ... other composables
                composable("cbct_viewer") {
                    currentTitle = "CBCT Viewer"
                    CbctViewerScreen(
                        onAnalysisComplete = { response -> activeCbctResponse = response },
                        onNavigateToSupport = { navController.navigate("explainable_ai") },
                        onNavigateToNerve = { navController.navigate("nerve_analysis") },
                        onNavigateToTumor = { navController.navigate("tumor_analysis") }
                    )
                }
                composable("prognosis") {
                    currentTitle = "Prognosis"
                    PrognosisScreen(
                        cbctResponse = activeCbctResponse,
                        onViewReport = { navController.navigate("reports") },
                        onAnalysisComplete = { response -> activePrognosisResponse = response }
                    )
                }
                composable("reports") {
                    currentTitle = "Clinical Reports"
                    ReportsScreen(
                        cbctResponse = activeCbctResponse,
                        prognosisResponse = activePrognosisResponse,
                        onNavigateToOralHealth = { navController.navigate("oral_health_review") }
                    )
                }
                composable("oral_health_review") {
                    currentTitle = "Oral Health Review"
                    OralHealthReviewScreen(cbctResponse = activeCbctResponse)
                }
                composable("training") {
                    currentTitle = "AI Engine Control"
                    TrainingScreen()
                }
                composable("admin_panel") {
                    currentTitle = "User Management"
                    AdminScreen()
                }
                composable("nerve_analysis") {
                    currentTitle = "IAN Tracing"
                    NerveAnalysisScreen(cbctResponse = activeCbctResponse)
                }
                composable("tumor_analysis") {
                    currentTitle = "Lesion Characterization"
                    TumorAnalysisScreen(cbctResponse = activeCbctResponse)
                }
                composable("settings") {
                    currentTitle = "System Settings"
                    SettingsScreen(onLogout = {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    })
                }
                composable("explainable_ai") {
                    currentTitle = "Decision Support"
                    ExplainableAIScreen(cbctResponse = activeCbctResponse)
                }
            }
        }
    }
}

@Composable
fun RowScope.BottomNavItem(navController: NavHostController, route: String, label: String, icon: ImageVector) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBarItem(
        selected = currentRoute == route,
        onClick = { navController.navigate(route) { launchSingleTop = true } },
        icon = { Icon(icon, null) },
        label = { Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
    )
}

@Composable
fun DrawerItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, fontWeight = FontWeight.Bold) },
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, null) },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = Secondary.copy(alpha = 0.1f),
            selectedIconColor = Secondary,
            selectedTextColor = Secondary
        )
    )
}

@Composable
fun DrawerHeader() {
    val userName = com.oralsurgeryai.app.data.UserSession.userName
    val userRole = com.oralsurgeryai.app.data.UserSession.userRole
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Secondary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = Secondary, modifier = Modifier.size(32.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(userName, style = MaterialTheme.typography.headlineMedium, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(userRole, style = MaterialTheme.typography.bodySmall, color = OnSurfaceVariant)
    }
}
