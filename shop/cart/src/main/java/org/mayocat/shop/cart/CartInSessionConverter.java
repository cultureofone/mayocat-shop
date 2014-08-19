/*
 * Copyright (c) 2012, Mayocat <hello@mayocat.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mayocat.shop.cart;

import org.mayocat.shop.cart.model.Cart;
import org.mayocat.shop.cart.model.CartInSession;
import org.xwiki.component.annotation.Role;

/**
 * @version $Id$
 */
@Role
public interface CartInSessionConverter
{
    CartInSession convertToCartInSession(Cart cart);

    Cart loadFromCartInSession(CartInSession cartInSession);
}