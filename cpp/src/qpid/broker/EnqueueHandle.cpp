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
 * \file EnqueueHandle.cpp
 */

#include "qpid/broker/EnqueueHandle.h"

#include "qpid/asyncStore/EnqueueHandleImpl.h"
#include "qpid/broker/PrivateImplRef.h"

namespace qpid {
namespace broker {

typedef PrivateImplRef<EnqueueHandle> PrivateImpl;

EnqueueHandle::EnqueueHandle(qpid::asyncStore::EnqueueHandleImpl* p) :
        Handle<qpid::asyncStore::EnqueueHandleImpl>()
{
    PrivateImpl::ctor(*this, p);
}

EnqueueHandle::EnqueueHandle(const EnqueueHandle& r) :
        Handle<qpid::asyncStore::EnqueueHandleImpl>()
{
    PrivateImpl::copy(*this, r);
}

EnqueueHandle::~EnqueueHandle() {
    PrivateImpl::dtor(*this);
}

EnqueueHandle&
EnqueueHandle::operator=(const EnqueueHandle& r) {
    return PrivateImpl::assign(*this, r);
}

// --- EnqueueHandleImpl methods ---

}} // namespace qpid::broker
