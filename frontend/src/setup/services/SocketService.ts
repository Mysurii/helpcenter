import io from 'socket.io-client'
import { envVariables } from '../config'
import { TMessage } from '@/common/types/message'
import { TConversation, TGroup } from '@/common/types/conversation'

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
  public onTakeover(callback: (data: TConversation) => void) {
    this.socket.on('handle_takeover', (data: TConversation) => callback(data))
  }
  public onFinishConversation(callback: (data: TConversation) => void) {
    this.socket.on('handle_finish_conversation', (data: TConversation) => callback(data))
  }
  public emitMessage(message: TMessage) {
    this.socket.emit('send_message', message)
  }
  public emitTyping(conversationId: string, typing: boolean) {
    this.socket.emit('is_typing', { conversationId, typing })
  }
  public emitTakeover(conversationId: string, members: TGroup) {
    this.socket.emit('takeover', { conversationId, members })
  }
  public emitFinishConversation(conversation: TConversation) {
    this.socket.emit('done', conversation)
  }
}

export default new SocketService()
