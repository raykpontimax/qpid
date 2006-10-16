/*
 *
 * Copyright (c) 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#ifndef _BlockingAPRAcceptor_
#define _BlockingAPRAcceptor_

#include <vector>
#include "apr-1/apr_network_io.h"
#include "apr-1/apr_poll.h"
#include "apr-1/apr_time.h"

#include "qpid/io/Acceptor.h"
#include "qpid/concurrent/APRMonitor.h"
#include "qpid/io/BlockingAPRSessionContext.h"
#include "qpid/concurrent/Runnable.h"
#include "qpid/io/SessionContext.h"
#include "qpid/io/SessionHandlerFactory.h"
#include "qpid/concurrent/Thread.h"
#include "qpid/concurrent/ThreadFactory.h"
#include "qpid/concurrent/ThreadPool.h"

namespace qpid {
namespace io {

    class BlockingAPRAcceptor : public virtual Acceptor
    {
        typedef std::vector<BlockingAPRSessionContext*>::iterator iterator;

        const bool debug;
        apr_pool_t* apr_pool;
        qpid::concurrent::ThreadFactory* threadFactory;
        std::vector<BlockingAPRSessionContext*> sessions;
	apr_socket_t* socket;
        const int connectionBacklog;
        volatile bool running;
        
    public:
	BlockingAPRAcceptor(bool debug = false, int connectionBacklog = 10);
        virtual int16_t bind(int16_t port);
        virtual int16_t getPort() const;
        virtual void run(SessionHandlerFactory* factory);
        virtual void shutdown();
	virtual ~BlockingAPRAcceptor();
        void closed(BlockingAPRSessionContext* session);
    };

}
}


#endif
