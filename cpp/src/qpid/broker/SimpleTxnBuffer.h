/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * \file SimpleTxnBuffer.h
 */

#ifndef qpid_broker_SimpleTxnBuffer_h_
#define qpid_broker_SimpleTxnBuffer_h_

#include "qpid/broker/TxnHandle.h"
#include "qpid/sys/Mutex.h"

#include <boost/shared_ptr.hpp>
#include <vector>

namespace qpid {
namespace broker {

class AsyncResultHandle;
class AsyncResultQueue;
class AsyncTransactionalStore;
class SimpleTxnOp;

class SimpleTxnBuffer {
public:
    SimpleTxnBuffer(AsyncResultQueue& arq);
    SimpleTxnBuffer(AsyncResultQueue& arq, std::string& xid);
    virtual ~SimpleTxnBuffer();
    TxnHandle& getTxnHandle();
    const std::string& getXid() const;
    bool is2pc() const;
    void incrOpCnt();
    void decrOpCnt();

    void enlist(boost::shared_ptr<SimpleTxnOp> op);
    bool prepare();
    void commit();
    void rollback();
    bool commitLocal(AsyncTransactionalStore* const store);

    // --- Async operations ---
    void asyncLocalCommit();
    static void handleAsyncCommitResult(const AsyncResultHandle* const arh);
    void asyncLocalAbort();
    static void handleAsyncAbortResult(const AsyncResultHandle* const arh);

private:
    mutable qpid::sys::Mutex m_opsMutex;
    mutable qpid::sys::Mutex m_submitOpCntMutex;
    mutable qpid::sys::Mutex m_completeOpCntMutex;
    static qpid::sys::Mutex s_uuidMutex;

    std::vector<boost::shared_ptr<SimpleTxnOp> > m_ops;
    TxnHandle m_txnHandle;
    AsyncTransactionalStore* m_store;
    AsyncResultQueue& m_resultQueue;
    std::string m_xid;
    bool m_tpcFlag;
    uint32_t m_submitOpCnt;
    uint32_t m_completeOpCnt;

    typedef enum {NONE = 0, PREPARE, COMMIT, ROLLBACK, COMPLETE} e_txnState;
    e_txnState m_state;

    uint32_t getNumOps() const;
    void createLocalXid();
};

}} // namespace qpid::broker

#endif // qpid_broker_SimpleTxnBuffer_h_
