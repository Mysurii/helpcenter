import { TLoginRequest } from '@/common/types/auth'
import BaseService from './BaseService'

class UserService extends BaseService {
  login = async (loginRequest: TLoginRequest): Promise<{ token: string }> => {
    const response = await this.api.post<{ token: string }>('/auth/signin', loginRequest)

    return response.data
  }
}

export default new UserService()
