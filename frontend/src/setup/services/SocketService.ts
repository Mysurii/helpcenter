import io from 'socket.io-client'
import { envVariables } from '../config'
import { TMessage } from '@/common/types/message'
import { TConversation } from '@/common/types/conversation'

class SocketService {
  private socket

  constructor() {
    this.socket = io(envVariables.apiKey)
  }

  public connect() {
    this.socket.connect()
  }

  public disconnect() {
    this.socket.disconnect()
  }

  public onRetrieveMessage(callback: (data: TMessage) => void) {
    this.socket.on('retrieve_message', (data) => callback(data))
  }

  public onIsTyping(callback: (data: boolean) => void) {
    this.socket.on('is_typing', (data) => callback(data))
  }
  public onNewConversation(callback: (data: TConversation) => void) {
    this.socket.on('new_conversation', (data: TConversation) => callback(data))
  }
  public emitMessage(message: string) {
    this.socket.emit('send_message', message)
  }
  public emitTyping(conversationId: string, typing: boolean) {
    this.socket.emit('is_typing', { conversationId, typing })
  }
}

export default new SocketService()
