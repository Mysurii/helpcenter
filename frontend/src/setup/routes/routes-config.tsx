import Home from "@/pages/Home";
import ProtectedRoute from "./protected-route";
import { useRoutes } from "react-router-dom";
import { Login, NotFound } from "@/pages";

const routesConfig = [
  {
    path: '/',
    element: <ProtectedRoute>
      <Home />
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