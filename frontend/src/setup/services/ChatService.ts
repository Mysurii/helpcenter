import { TConversation } from '@/common/types/conversation'
import BaseService from './BaseService'
import { TMessage } from '@/common/types/message'

class ChatService extends BaseService {
  fetchConversations = async () => {
    const response = await this.api.get<TConversation[]>('/conversations')
    return response.data
  }

  fetchMessages = async (id: string) => {
    const response = await this.api.get<TMessage[]>(`/conversations/${id}`)
    console.log(response)
    return response.data
  }
}

export default new ChatService()
