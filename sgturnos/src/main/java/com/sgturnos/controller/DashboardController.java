@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    public String mostrarDashboard(Model model) {
        // Aquí puedes pasar variables al HTML con model.addAttribute si deseas
        return "DashboardMain/dashboard"; // nombre de la vista sin .html
    }
}