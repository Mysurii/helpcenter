import { ReactNode, FC } from 'react';
import { Roles } from '@/common/types/roles'
import { Navigate } from 'react-router-dom';

interface IProps {
  expectedRoles?: Roles[],
  children: ReactNode
}

const ProtectedRoute: FC<IProps> = ( { expectedRoles, children } ) => {
  const isAuthorized = true;
  const role = 'admin'
  const areRolesRequired = !!expectedRoles?.length

  const rolesMatch = areRolesRequired ? expectedRoles.includes( role ) : true

  if ( !isAuthorized || !rolesMatch ) {
    return <Navigate to="/" replace />
  }


  return children
}

export default ProtectedRoute