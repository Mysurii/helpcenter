import { TUser } from '@/common/types/user';
import { create } from 'zustand'
import jwtDecode from 'jwt-decode'

interface AuthState {
  accessToken: string | null,
  user: TUser | null
  decodeToken: ( token: string ) => void
  init: () => void
  clearToken: () => void
}

const useAuthStore = create<AuthState>()( ( set ) => ( {
  accessToken: null,
  user: null,
  init: () => {
    const accessToken = localStorage.getItem( 'access_token' )
    set( { accessToken } )
  },
  decodeToken: ( token: string ) => {
    set( { user: jwtDecode<TUser>( token ) } )
    localStorage.setItem( 'access_token', token )
  },
  clearToken: () => {
    localStorage.clear()
    set( { user: null, accessToken: null } )
  }
} ) )

export default useAuthStore