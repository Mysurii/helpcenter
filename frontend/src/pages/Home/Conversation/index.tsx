import React from 'react'
import Message from './Message'

const message = {
  text: 'Hello world!',
  sender: {
    _id: '213',
    name: 'John Doe',
    email: 'joihnDoe@hotmail.com'
  },
  createdAt: new Date(),
  conversationId: '123'
}


function Conversation () {
  return (
    <div className='p-8 h-[calc(100vh-60px)] overflow-y-auto space-y-8 w-full' >
      <Message message={message} />
      <Message message={message} />
    </div>
  )
}

export default Conversation