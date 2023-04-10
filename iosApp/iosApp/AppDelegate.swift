import UIKit
import shared

@UIApplicationMain
struct AppDelegate: UIResponder, UIApplicationDelegate {

    init() {
        DependencyInjectionKt.setup(declaration: { _ in })
    }

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        window = UIWindow(frame: UIScreen.main.bounds)
        let mainViewController = MainViewControllerKt.MainViewController()
        window?.rootViewController = mainViewController
        window?.makeKeyAndVisible()
        return true
    }
}
