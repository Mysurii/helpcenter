import { ReactNode, FC } from 'react';
import { TRoles } from '@/common/types/roles'
import { Navigate } from 'react-router-dom';
import useAuthStore from '../stores/auth.store';

interface IProps {
  expectedRoles?: TRoles[],
  children: ReactNode
}

const ProtectedRoute: FC<IProps> = ( { expectedRoles, children } ) => {
  const isAuthorized = true;
  const { user } = useAuthStore()
  const areRolesRequired = !!expectedRoles?.length


  if ( !user ) {
    return <Navigate to="/signin" replace />
  }

  const rolesMatch = areRolesRequired ? expectedRoles.includes( user?.role ) : true

  if ( !isAuthorized || !rolesMatch ) {
    return <Navigate to="/signin" replace />
  }


  return children
}

export default ProtectedRoute