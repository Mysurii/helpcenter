import { TLoginRequest } from '@/common/types/auth'
import BaseService from './BaseService'
import { TUser } from '@/common/types/user'

class UserService extends BaseService {
  login = async (loginRequest: TLoginRequest): Promise<{ token: string }> => {
    const response = await this.api.post<{ token: string }>('/auth/signin', loginRequest)
    return response.data
  }

  signup = async (loginRequest: TLoginRequest): Promise<{ token: string }> => {
    const response = await this.api.post<{ token: string }>('/auth/signin', loginRequest)
    return response.data
  }

  getPortalUsers = async (): Promise<TUser[]> => {
    const response = await this.api.get<TUser[]>('/auth/users')
    return response.data
  }

  deleteUser = async (id: string): Promise<boolean> => {
    const response = await this.api.delete<boolean>(`/auth/users/${id}`)
    return response.data
  }
}

export default new UserService()
