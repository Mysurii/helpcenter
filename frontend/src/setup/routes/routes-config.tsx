import Home from "@/pages/Home";
import ProtectedRoute from "./protected-route";
import { useRoutes } from "react-router-dom";
import { Login, NotFound } from "@/pages";
import Dashboard from "@/pages/Dashboard.ts";

const routesConfig = [
  {
    path: '/',
    element: <ProtectedRoute>
      <Home />
    </ProtectedRoute>
  },
  {
    path: '/dashboard',
    element: <ProtectedRoute>
      <Dashboard />
    </ProtectedRoute>
  },
  {
    path: '/signin',
    element:
      <Login />
  },
  {
    path: '*',
    element: <NotFound />
  },
]

const RouteRenderer = () => {
  // eslint-disable-next-line react-hooks/rules-of-hooks
  const routes = useRoutes( routesConfig )
  return routes
}

export default RouteRenderer